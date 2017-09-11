package dev.kkorolyov.pancake.skillet.utility;

import dev.kkorolyov.pancake.skillet.data.Attribute;
import dev.kkorolyov.pancake.skillet.data.Component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import static dev.kkorolyov.pancake.skillet.utility.Data.isNumber;

/**
 * Provides methods for reading and writing data from and to files.
 */
public final class Persister {
	private Persister() {}

	/**
	 * Writes components to a resource.
	 * Components are written in the format expected by {@link #loadComponents(Path)}.
	 * @param components saved components
	 * @param path path to resource
	 */
	public static void saveComponents(Collection<Component> components, Path path) {
		try (BufferedWriter out = Files.newBufferedWriter(path)) {
			for (Component component : components) {
				out.write(componentToString(component));
				out.newLine();
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	/**
	 * Parses components from a resource.
	 * Each line in the file is expected to be in the format:
	 * <pre>
	 *   {componentName}={attributeName}: {attributeValue}...
	 * </pre>
	 * i.e. a component name mapped to an arbitrary list of attributes.
	 * Valid attribute value types include:
	 * <pre>
	 *   Number - 123, 1.23
	 *   String - "someText"
	 *   Map - {key=value, key=value, key=value...}
	 * </pre>
	 * @param path path to resource
	 * @return parsed components
	 */
	public static Collection<Component> loadComponents(Path path) {
		List<Component> components = new ArrayList<>();

		try (BufferedReader in = Files.newBufferedReader(path)) {
			String line;
			while ((line = in.readLine()) != null) {
				components.add(stringToComponent(line));
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return components;
	}

	public static String componentToString(Component component) {
		StringJoiner joiner = new StringJoiner(", ", (component.getName() + " = "), "");

		for (Attribute attribute : component.getAttributes()) {
			joiner.add(attributeToString(attribute));
		}
		return joiner.toString();
	}
	public static Component stringToComponent(String s) {
		String[] split = s.split("\\s*=\\s*", 2);
		String name = split[0], attributesS = split[1];

		Component component = new Component(name);
		for (String attributeS : attributesS.split(",\\s?(?![^{]*})")) {
			component.addAttribute(stringToAttribute(attributeS));
		}
		return component;
	}

	public static String attributeToString(Attribute attribute) {
		return attribute.getName() + ": " + attribute.getValue();
	}
	public static Attribute stringToAttribute(String s) {
		String[] split = s.split(":\\s?", 2);
		String name = split[0], valueS = split[1];

		return new Attribute(name, ValueParser.getStrategy(valueS).parse(valueS));
	}

	private static abstract class ValueParser {
		private static final Collection<ValueParser> strategies = Arrays.asList(
				new NumberParser(),
				new StringParser(),
				new MapParser()
		);

		private static ValueParser getStrategy(String s) {
			return strategies.stream()
					.filter(strategy -> strategy.accepts(s))
					.findFirst()
					.orElseThrow(() -> new UnsupportedOperationException("No parsing strategy for: " + s));
		}

		abstract Object parse(String s);

		abstract boolean accepts(String s);

		private static class NumberParser extends ValueParser {
			@Override
			Object parse(String s) {
				try {
					return NumberFormat.getInstance().parse(s);
				} catch (ParseException e) {
					throw new RuntimeException(e);
				}
			}

			@Override
			boolean accepts(String s) {
				return isNumber(s);
			}
		}

		private static class StringParser extends ValueParser {
			@Override
			Object parse(String s) {
				return s.substring(1, s.length() - 1);
			}

			@Override
			boolean accepts(String s) {
				return s.matches("^\".*\"$");
			}
		}

		private static class MapParser extends ValueParser {
			@Override
			Object parse(String s) {
				Map<String, Object> map = new LinkedHashMap<>();

				String[] pairs = s.substring(1, s.length() - 1).split(",\\s?");
				for (String pair : pairs) {
					String[] splitPair = pair.split("\\s*=\\s*", 2);
					String name = splitPair[0], valueS = splitPair[1];

					map.put(name, ValueParser.getStrategy(valueS).parse(valueS));
				}
				return map;
			}

			@Override
			boolean accepts(String s) {
				return s.matches("\\{.*}");
			}
		}
	}
}
