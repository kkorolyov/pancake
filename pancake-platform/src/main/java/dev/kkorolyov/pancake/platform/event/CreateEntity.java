package dev.kkorolyov.pancake.platform.event;

import dev.kkorolyov.pancake.platform.entity.Component;

/**
 * {@link Event} requesting an entity be created.
 */
public class CreateEntity implements Event {
	private final Iterable<Component> components;

	/**
	 * Constructs a new create entity event.
	 * @param components initial entity components
	 */
	public CreateEntity(Iterable<Component> components) {
		this.components = components;
	}

	/** @return initial entity components */
	public Iterable<Component> getComponents() {
		return components;
	}
}
