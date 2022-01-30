package dev.kkorolyov.pancake.core.event;

import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.event.Event;
import dev.kkorolyov.pancake.platform.math.Vector2;
import dev.kkorolyov.pancake.platform.math.Vectors;

/**
 * {@link Event} broadcast when entities intersect.
 */
public final class EntitiesIntersected implements Event {
	private final Entity a, b;
	private final Vector2 mtv;

	/**
	 * Constructs a new intersection event between entities {@code a} and {@code b} with the {@code a}-relative normalized {@code mtv} used to remove overlap.
	 */
	public EntitiesIntersected(Entity a, Entity b, Vector2 mtv) {
		this.a = a;
		this.b = b;
		this.mtv = Vectors.create(mtv);
	}

	/**
	 * Returns the main intersecting entity.
	 */
	public Entity getA() {
		return a;
	}
	/**
	 * Returns the secondary intersecting entity.
	 */
	public Entity getB() {
		return b;
	}
	/**
	 * Returns the minimum translation vector used to separate overlapping entities relative to {@code a}.
	 */
	public Vector2 getMtv() {
		return mtv;
	}
}
