package dev.kkorolyov.pancake.skillet.utility;

import dev.kkorolyov.pancake.skillet.data.Attribute;
import dev.kkorolyov.pancake.skillet.data.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Provides various convenience methods for manipulating abstract data.
 */
public final class Data {
	private Data() {}

	/** @return {@code true} if {@code s} is a valid number */
	public static boolean isNumber(String s) {
		return s.matches("[+-]?(\\d+\\.\\d+|\\d+)");
	}
	/** @return {@code true} if {@code s} is a valid number or an empty string */
	public static boolean isNumberOrEmpty(String s) {
		return s.matches("[+-]?(\\d+\\.\\d+|\\d+)?");
	}

	/**
	 * Clones an object by serializing and deserializing it.
	 * @param o object to clone
	 * @return clone of {@code o}
	 */
	public static <T extends Serializable> T serialClone(T o) {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		try (ObjectOutputStream oo = new ObjectOutputStream(bo)) {
			oo.writeObject(o);
			oo.close();

			try (ObjectInputStream oi = new ObjectInputStream(new ByteArrayInputStream(bo.toByteArray()))) {
				return (T) oi.readObject();
			}
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public static String attributeToString(Attribute attribute) {
		return attribute.getName() + ": " + attribute.getValue();
	}
	public static Attribute stringToAttribute(String s) {
		String[] split = s.split(":\\s?", 2);
		String name = split[0], valueS = split[1];

		return new Attribute(name, ValueParser.getStrategy(valueS).parse(valueS));
	}

	// public static String componentToString(Component component) {
	//
	// }
	public static Component stringToComponent(String s) {
		String[] split = s.split("\\s*=\\s*", 2);
		String name = split[0], attributesS = split[1];

		Component component = new Component(name);
		for (String attributeS : attributesS.split(",\\s?(?![^{]*})")) {
			component.addAttribute(stringToAttribute(attributeS));
		}
		return component;
	}
	//
	// public static String entityToString(Entity entity) {
	//
	// }
	// public static Entity stringToEntity(String s) {
	//
	// }

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
