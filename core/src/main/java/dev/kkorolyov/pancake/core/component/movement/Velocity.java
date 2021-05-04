package dev.kkorolyov.pancake.core.component.movement;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.math.Vectors;

/**
 * Velocity of a moving entity.
 */
public final class Velocity implements Component {
	private final Vector3 value;

	/**
	 * Constructs a new velocity.
	 * @param value initial value in {@code m/s}
	 */
	public Velocity(Vector3 value) {
		this.value = Vectors.create(value);
	}

	/**
	 * Applies a positional change calculated from velocity and duration to a point.
	 * @param position position to move
	 * @param seconds seconds used in movement calculation
	 */
	public void move(Vector3 position, double seconds) {
		position.add(value, seconds);
	}

	/** @return velocity in {@code m/s} */
	public Vector3 getValue() {
		return value;
	}
}
