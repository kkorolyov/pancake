package dev.kkorolyov.pancake.skillet;

import dev.kkorolyov.pancake.platform.storage.Component;
import dev.kkorolyov.pancake.platform.storage.Storable;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Provides fresh instances of various components by name.
 */
public class ComponentFactory extends Storable<ComponentFactory> {
	private final Map<String, Component> components = new LinkedHashMap<>();

	/**
	 * Provides a copy of a component.
	 * @param name name of component to copy
	 * @return clone of component of name {@code name}, or {@code null} if no such component
	 */
	public Component get(String name) {
		return Optional.of(components.get(name))
				.map(Storable::copy)
				.orElse(null);
	}

	/** @return names of all components */
	public Collection<String> getNames() {
		return components.keySet();
	}

	/** @return {@code true} if this factory contains a component with name {@code name} */
	public boolean contains(String name) {
		return components.containsKey(name);
	}

	/**
	 * Adds a component to this factory.
	 * @param component component to add
	 * @param overwrite {@code true} overwrites any existing component with the same name
	 * @return {@code this}
	 */
	public ComponentFactory add(Component component, boolean overwrite) {
		if (overwrite || !contains(component.getName())) {
			components.put(component.getName(), component);

			changed(ComponentFactoryChangeEvent.ADD);
		}
		return this;
	}
	/**
	 * Adds multiple components.
	 * @param components components to add
	 * @param overwrite {@code true} overwrites any existing component with the same name
	 * @return {@code this}
	 */
	public ComponentFactory add(Iterable<Component> components, boolean overwrite) {
		for (Component component : components) add(component, overwrite);
		return this;
	}

	/**
	 * @param name removed component name
	 * @return removed component, or {@code null} if no such component
	 */
	public Component remove(String name) {
		Component removed = components.remove(name);

		if (removed != null) changed(ComponentFactoryChangeEvent.REMOVE);

		return removed;
	}

	/**
	 * Removes all components.
	 */
	public void clear() {
		components.clear();
	}

	public enum ComponentFactoryChangeEvent implements StorableChangeEvent {
		ADD,
		REMOVE
	}
}
