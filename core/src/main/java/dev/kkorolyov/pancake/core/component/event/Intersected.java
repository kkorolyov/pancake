package dev.kkorolyov.pancake.core.component.event;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.math.Vector2;

/**
 * Notes that an intersection with another entity occurred.
 */
public final class Intersected implements Component {
	private final Entity other;
	private final Vector2 mtv;

	/**
	 * Constructs a new intersected event with {@code other} including owning-entity-relative normalized {@code mtv} used to remove overlap.
	 */
	public Intersected(Entity other, Vector2 mtv) {
		this.other = other;
		this.mtv = Vector2.of(mtv);
	}

	/**
	 * Returns the entity intersecting with the owning entity.
	 */
	public Entity getOther() {
		return other;
	}

	/**
	 * Returns the minimum translation vector used to separate entities relative to the owning entity.
	 */
	public Vector2 getMtv() {
		return mtv;
	}
}
