package dev.kkorolyov.pancake.muffin.persistence;

import dev.kkorolyov.pancake.muffin.data.type.Attribute;
import dev.kkorolyov.pancake.muffin.data.type.Component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;

/**
 * Deals with persistence of components.
 */
public class ComponentPersister extends DataPersister<Component> {
	private final AttributePersister attributePersister = new AttributePersister();

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
	public Collection<Component> loadComponents(Path path) {
		List<Component> components = new ArrayList<>();

		try (BufferedReader in = Files.newBufferedReader(path)) {
			String line;
			while ((line = in.readLine()) != null) {
				components.add(read(line));
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return components;
	}
	/**
	 * Writes components to a resource.
	 * Components are written in the format expected by {@link #loadComponents(Path)}.
	 * @param components saved components
	 * @param path path to resource
	 */
	public void saveComponents(Collection<Component> components, Path path) {
		try (BufferedWriter out = Files.newBufferedWriter(path)) {
			for (Component component : components) {
				out.write(write(component));
				out.newLine();
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Override
	public Component read(String s) {
		String[] split = s.split("\\s*=\\s*", 2);
		String name = split[0], attributesS = split[1];

		Component component = new Component(name);
		for (String attributeS : attributesS.split(",\\s?(?![^{]*})")) {
			component.addAttribute(attributePersister.read(attributeS));
		}
		return component;
	}
	@Override
	public String write(Component component) {
		StringJoiner joiner = new StringJoiner(", ", (component.getName() + " = "), "");

		for (Attribute attribute : component.getAttributes()) {
			joiner.add(attributePersister.write(attribute));
		}
		return joiner.toString();
	}
}
