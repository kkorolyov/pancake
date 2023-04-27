package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.math.Vector3;

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
		this.value = Vector3.of(value);
	}

	/**
	 * Applies an acceleration calculated from net force, mass, and duration to a velocity vector.
	 * @param velocity velocity to accelerate
	 * @param mass mass used in acceleration calculation
	 * @param seconds seconds used in acceleration calculation
	 */
	public void accelerate(Vector3 velocity, double mass, double seconds) {
		velocity.add(value, seconds / mass);  // a = f/m, v' = v + at
	}

	/** @return force in {@code N} */
	public Vector3 getValue() {
		return value;
	}
}
