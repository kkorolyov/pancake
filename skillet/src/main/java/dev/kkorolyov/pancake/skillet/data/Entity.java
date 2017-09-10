package dev.kkorolyov.pancake.skillet.data;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A container of components.
 */
public class Entity extends DataObservable<Entity> {
	private String name;
	private final Map<String, Component> components = new LinkedHashMap<>();

	/**
	 * Constructs a new entity with an empty name.
	 */
	public Entity() {}
	/**
	 * Constructs a new entity.
	 * @param name entity name
	 */
	public Entity(String name) {
		this.name = name;
	}

	/**
	 * @param name name to search by
	 * @return {@code true} if this entity contains a component of name {@code name}
	 */
	public boolean containsComponent(String name) {
		return components.containsKey(name);
	}

	/**
	 * @param component added component
	 * @return {@code this}
	 */
	public Entity addComponent(Component component) {
		components.put(component.getName(), component);

		changed(EntityChangeEvent.ADD);

		return this;
	}
	/**
	 * @param name removed component name
	 * @return removed component, or {@code null} if no such component
	 */
	public Component removeComponent(String name) {
		Component oldComponent = components.remove(name);

		changed(EntityChangeEvent.REMOVE);

		return oldComponent;
	}

	/** @return entity name */
	public String getName() {
		return name;
	}
	/** @param name entity name */
	public void setName(String name) {
		this.name = name;

		changed(EntityChangeEvent.NAME);
	}
	/** @return	entity components */
	public Collection<Component> getComponents() {
		return components.values();
	}

	public enum EntityChangeEvent implements DataChangeEvent {
		NAME,
		ADD,
		REMOVE
	}
}
