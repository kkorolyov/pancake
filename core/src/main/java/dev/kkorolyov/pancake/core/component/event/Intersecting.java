package dev.kkorolyov.pancake.core.component.event;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.math.Vector2;
import dev.kkorolyov.pancake.platform.math.Vectors;

/**
 * Notes that an intersection with another entity occurred.
 */
public final class Intersecting implements Component {
	private final Entity other;
	private final Vector2 mtv;

	/**
	 * Constructs a new intersected event with {@code other} and owning-entity-relative normalized {@code mtv} used to remove overlap.
	 */
	public Intersecting(Entity other, Vector2 mtv) {
		this.other = other;
		this.mtv = Vectors.create(mtv);
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
