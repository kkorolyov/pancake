package dev.kkorolyov.pancake.platform.event;

/**
 * {@link Event} requesting an entity be destroyed.
 */
public class DestroyEntity implements Event {
	private final int id;

	/**
	 * Constructs a new destroy entity event.
	 * @param id ID of entity to destroy
	 */
	public DestroyEntity(int id) {
		this.id = id;
	}

	/** @return ID of entity to destroy */
	public int getId() {
		return id;
	}
}
