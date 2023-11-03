package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.utility.ArgVerify;

/**
 * Damping applied on an entity.
 * Damping values are in the interval {@code [0, 1]}, essentially translating to {@code [immediate stop, no damping]}.
 */
public final class Damping implements Component {
	private static final double EFFECTIVE_ZERO = 1e-9;
	private final Vector3 value;

	/**
	 * Constructs a new damping.
	 * @param value damping to apply per component
	 */
	public Damping(Vector3 value) {
		this.value = Vector3.of(verify(value.getX()), verify(value.getY()), verify(value.getZ()));
	}
	private static double verify(double value) {
		return ArgVerify.betweenInclusive("damping", 0.0, 1.0, value);
	}

	/**
	 * Damps a velocity vector along axes where applied force is zero or in the opposite direction of velocity.
	 * Damping is done by multiplying a velocity component by the respective damping component, so, the smaller the damping value, the greater the decrease in velocity.
	 * @param velocity damped velocity
	 * @param force applied force
	 */
	public void damp(Vector3 velocity, Vector3 force) {
		velocity.setX(velocity.getX() * damp(velocity.getX(), force.getX(), value.getX()));
		velocity.setY(velocity.getY() * damp(velocity.getY(), force.getY(), value.getY()));
		velocity.setZ(velocity.getZ() * damp(velocity.getZ(), force.getZ(), value.getZ()));
	}
	private static double damp(double velocity, double force, double damping) {
		return (velocity < 0 ? force >= -EFFECTIVE_ZERO : force <= EFFECTIVE_ZERO) ? damping : 1;
	}

	/**
	 * Returns the damping vector.
	 */
	public Vector3 getValue() {
		return value;
	}
}
