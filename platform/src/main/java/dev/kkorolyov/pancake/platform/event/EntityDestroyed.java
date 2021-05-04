package dev.kkorolyov.pancake.platform.event;

/**
 * {@link Event} broadcast when an entity is destroyed.
 */
public class EntityDestroyed implements Event {
	private final int id;

	/**
	 * Constructs a new entity destroyed event.
	 * @param id ID of destroyed entity
	 */
	public EntityDestroyed(int id) {
		this.id = id;
	}

	/** @return ID of destroyed entity */
	public int getId() {
		return id;
	}
}
