package dev.kkorolyov.pancake.skillet.data;

import dev.kkorolyov.simpleprops.Properties;

import java.util.LinkedHashMap;
import java.util.Map;

import static dev.kkorolyov.pancake.skillet.utility.Data.serialClone;

/**
 * Provides fresh instances of various components by name.
 */
public class ComponentFactory {
	private final Map<String, Component> components = new LinkedHashMap<>();

	/**
	 * Provides a clone of a component by serializing, then deserializing its base instance.
	 * @param name name of component to clone
	 * @return clone of component of name {@code name}, or {@code null} if no such component
	 */
	public Component get(String name) {
		Component base = components.get(name);
		if (base == null) return null;

		return serialClone(base);
	}

	/** @return names of all components */
	public Iterable<String> getNames() {
		components.put("Testo", new Component("Testo"));
		return components.keySet();
	}

	/**
	 * Parses components from a config file.
	 * Each property in the file is expected to be in the format:
	 * <pre>
	 *   {componentName}={attributeName}: {attributeValue}...
	 * </pre>
	 * i.e. a component name mapped to an arbitrary list of attributes.
	 * Valid attribute value types include:
	 * <pre>
	 *   String - AnyTextNotParseableAsANumber
	 *   Number - 123, 1.23
	 *   Map - {key: value, key: value, key: value...}
	 * </pre>
	 * @param componentConfig component config properties
	 */
	public void put(Properties componentConfig) {

	}

	/**
	 * Clears all components.
	 */
	public void clear() {
		components.clear();
	}
}
