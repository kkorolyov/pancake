package dev.kkorolyov.pancake.core.event;

import dev.kkorolyov.pancake.platform.event.Event;

/**
 * {@link Event} broadcast when entities collide.
 */
public class EntitiesCollided implements Event {
	private final int[] collided;

	/**
	 * Constructs a new entities collided event.
	 * @param collided IDs of collided entities
	 */
	public EntitiesCollided(int... collided) {
		this.collided = collided;
	}

	/** @return IDs of collided entities */
	public int[] getCollided() {
		return collided;
	}
}
