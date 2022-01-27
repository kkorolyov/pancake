package dev.kkorolyov.pancake.core.component.movement;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.math.Vectors;
import dev.kkorolyov.pancake.platform.utility.ArgVerify;

/**
 * Maximum velocity of a moving entity.
 */
public final class VelocityCap implements Component {
	private final Vector3 value;

	/**
	 * Constructs a new velocity cap.
	 * @param value vector defining maximum speed
	 */
	public VelocityCap(Vector3 value) {
		this.value = Vectors.create(verify(value.getX()), verify(value.getY()), verify(value.getZ()));
	}
	private static double verify(double value) {
		return ArgVerify.greaterThanEqual("cap", 0, value);
	}

	/**
	 * Caps the speed along each component of {@code velocity}.
	 */
	public void cap(Vector3 velocity) {
		velocity.setX(cap(round(velocity.getX()), value.getX()));
		velocity.setY(cap(round(velocity.getY()), value.getY()));
		velocity.setZ(cap(round(velocity.getZ()), value.getZ()));
	}
	private static double cap(double velocity, double maxSpeed) {
		return velocity < 0 ? Math.max(velocity, -maxSpeed) : Math.min(velocity, maxSpeed);
	}
	private static double round(double value) {
		return Double.compare(0, value) == 0 ? 0 : value;
	}

	/**
	 * Returns maximum velocity in {@code m/s}.
	 */
	public Vector3 getValue() {
		return value;
	}
}
