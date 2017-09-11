package dev.kkorolyov.pancake.skillet.data;

import dev.kkorolyov.pancake.skillet.utility.Persister;

import java.nio.file.Path;
import java.util.Collection;
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
	 * See {@link Persister#loadComponents(Path)} for appropriate file formatting.
	 * @param componentConfig path to component config
	 * @return all parsed components
	 */
	public Collection<Component> add(Path componentConfig) {
		Collection<Component> added = Persister.loadComponents(componentConfig);
		for (Component component : added) {
			add(component);
		}
		return added;
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
