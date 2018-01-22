package dev.kkorolyov.pancake.platform.event;

/**
 * {@link Event} broadcast when an entity is created.
 */
public class EntityCreated implements Event {
	private final int id;

	/**
	 * Constructs a new entity created event.
	 * @param id ID of created entity
	 */
	public EntityCreated(int id) {
		this.id = id;
	}

	/** @return ID of created entity */
	public int getId() {
		return id;
	}
}
