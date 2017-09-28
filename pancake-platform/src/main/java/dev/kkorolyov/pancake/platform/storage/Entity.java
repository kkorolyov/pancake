package dev.kkorolyov.pancake.platform.storage;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A container of distinct components.
 */
public class Entity extends Storable<Entity> implements Iterable<Component> {
	private static final long serialVersionUID = -6259009537483472204L;

	private String name;
	private final Map<String, Component> components = new LinkedHashMap<>();

	/**
	 * Constructs a new entity with an empty name and no components.
	 */
	public Entity() {}
	/**
	 * Constructs a new entity with no components.
	 * @param name entity name
	 */
	public Entity(String name) {
		this(name, Collections.emptyList());
	}
	/**
	 * Constructs a new entity.
	 * @param name entity name
	 * @param components entity components
	 */
	public Entity(String name, Iterable<Component> components) {
		this.name = name;
		for (Component component : components) addComponent(component);
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
	 * @param components components to add
	 * @return {@code this}
	 */
	public Entity addAll(Iterable<Component> components) {
		components.forEach(this::addComponent);
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

	@Override
	public Iterator<Component> iterator() {
		return getComponents().iterator();
	}

	/**
	 * A change to an entity.
	 */
	public enum EntityChangeEvent implements StorableChangeEvent {
		NAME,
		ADD,
		REMOVE
	}
}
