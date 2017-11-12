package dev.kkorolyov.pancake.skillet.model.factory;

import dev.kkorolyov.pancake.skillet.model.GenericComponent;
import dev.kkorolyov.pancake.skillet.model.Model;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Provides fresh instances of various components by name.
 */
public class ComponentFactory extends Model<ComponentFactory> {
	private final Map<String, GenericComponent> components = new LinkedHashMap<>();

	/**
	 * Provides a copy of a component.
	 * @param name name of component to copy
	 * @return clone of component of name {@code name}, or {@code null} if no such component
	 */
	public GenericComponent get(String name) {
		return Optional.of(components.get(name))
				.map(GenericComponent::copy)
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
	public ComponentFactory add(GenericComponent component, boolean overwrite) {
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
	public ComponentFactory add(Iterable<GenericComponent> components, boolean overwrite) {
		for (GenericComponent component : components) add(component, overwrite);
		return this;
	}

	/**
	 * @param name removed component name
	 * @return removed component, or {@code null} if no such component
	 */
	public GenericComponent remove(String name) {
		GenericComponent removed = components.remove(name);

		if (removed != null) changed(ComponentFactoryChangeEvent.REMOVE);

		return removed;
	}

	/**
	 * Removes all components.
	 */
	public void clear() {
		components.clear();

		changed(ComponentFactoryChangeEvent.REMOVE);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || !getClass().isAssignableFrom(obj.getClass())) return false;

		ComponentFactory o = (ComponentFactory) obj;
		return Objects.equals(components, o.components);
	}
	@Override
	public int hashCode() {
		return Objects.hash(components);
	}

	public enum ComponentFactoryChangeEvent implements ModelChangeEvent {
		ADD,
		REMOVE
	}
}
