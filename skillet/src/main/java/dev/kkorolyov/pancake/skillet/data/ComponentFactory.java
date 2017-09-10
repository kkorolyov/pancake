package dev.kkorolyov.pancake.skillet.data;

import dev.kkorolyov.simpleprops.Properties;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import static dev.kkorolyov.pancake.skillet.utility.Data.serialClone;
import static dev.kkorolyov.pancake.skillet.utility.Data.stringToComponent;

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
	public Collection<String> getNames() {
		return components.keySet();
	}

	/**
	 * @param component added component
	 * @return {@code this}
	 */
	public ComponentFactory add(Component component) {
		components.put(component.getName(), component);
		return this;
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
	 * @return {@code this}
	 */
	public ComponentFactory add(Properties componentConfig) {
		for (Entry<String, String> entry : componentConfig) {
			components.put(entry.getKey(), stringToComponent(entry.getKey() + "=" + entry.getValue()));
		}
		return this;
	}

	/**
	 * @param name removed component name
	 * @return removed component, or {@code null} if no such component
	 */
	public Component remove(String name) {
		return components.remove(name);
	}

	/**
	 * Removes all components.
	 */
	public void clear() {
		components.clear();
	}
}
