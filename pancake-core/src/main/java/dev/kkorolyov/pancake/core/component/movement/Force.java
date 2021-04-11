package dev.kkorolyov.pancake.core.component.movement;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.math.Vectors;

/**
 * Net force acting on an entity.
 */
public final class Force implements Component {
	private final Vector3 value;

	/**
	 * Constructs a new force.
	 * @param value initial value in {@code N}
	 */
	public Force(Vector3 value) {
		this.value = Vectors.create(value);
	}

	/**
	 * Applies an acceleration calculated from net force, mass, and duration to a velocity vector.
	 * @param velocity velocity to accelerate
	 * @param mass mass used in acceleration calculation
	 * @param seconds seconds used in acceleration calculation
	 */
	public Vector3 accelerate(Vector3 velocity, double mass, double seconds) {
		velocity.add(value, seconds / mass);  // a = f/m, v' = v + at
		return velocity;
	}

	/** @return force in {@code N} */
	public Vector3 getValue() {
		return value;
	}
}
