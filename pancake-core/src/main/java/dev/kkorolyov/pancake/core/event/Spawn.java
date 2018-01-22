package dev.kkorolyov.pancake.core.event;

import dev.kkorolyov.pancake.platform.event.Event;

/**
 * {@link Event} requesting a spawner to spawn.
 */
public class Spawn implements Event {
	private final int id;

	/**
	 * Constructs a new spawn event.
	 * @param id ID of entity to use spawn component of
	 */
	public Spawn(int id) {
		this.id = id;
	}

	/** @return ID of entity to use spawn component of */
	public int getId() {
		return id;
	}
}
