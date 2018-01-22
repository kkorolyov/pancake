package dev.kkorolyov.killstreek.event;

import dev.kkorolyov.killstreek.component.Damage;
import dev.kkorolyov.pancake.platform.event.Event;

/**
 * {@link Event} requesting a {@link Damage} component to be reset.
 */
public class ResetDamage implements Event {
	private final int id;

	/**
	 * Constructs a new reset damage event.
	 * @param id ID of entity to reset damage component of
	 */
	public ResetDamage(int id) {
		this.id = id;
	}


	/** @return ID of entity to reset damage component of */
	public int getId() {
		return id;
	}
}
