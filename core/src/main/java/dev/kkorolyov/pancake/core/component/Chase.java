package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.math.FloatOps;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.utility.ArgVerify;

/**
 * An intended position and maximum strength used to reach that position.
 * Represents a location an entity wants to get to.
 */
public final class Chase implements Component {
	private final Vector3 target;
	private final Vector3 strength;
	private double buffer;

	/**
	 * Constructs a new chase component with position {@code target}, max {@code strength} of force to apply per component, and {@code buffer} distance around target considered as reached.
	 */
	public Chase(Vector3 target, Vector3 strength, double buffer) {
		this.target = Vector3.of(target);
		this.strength = Vector3.of(verify(strength.getX()), verify(strength.getY()), verify(strength.getZ()));
		setBuffer(buffer);
	}
	private static double verify(double strength) {
		return ArgVerify.greaterThanEqual("strength", 0.0, strength);
	}

	/**
	 * Sets strength of a force vector along axes where the current force is less than the chase strength in the direction of the chase target.
	 */
	public void chase(Vector3 position, Vector3 force) {
		if (Vector3.distance(position, target) > buffer) {
			// alter per component to avoid reducing current force
			force.setX(chase(target.getX() - position.getX(), force.getX(), strength.getX()));
			force.setY(chase(target.getY() - position.getY(), force.getY(), strength.getY()));
			force.setZ(chase(target.getZ() - position.getZ(), force.getZ(), strength.getZ()));
		}
	}
	private static double chase(double currentDistance, double currentForce, double strength) {
		return FloatOps.equals(currentDistance, 0)
				? currentForce
				// negative distance implies negative force required to reach
				: currentDistance < 0 ? Math.min(currentForce, -strength) : Math.max(currentForce, strength);
	}

	/**
	 * Returns the position target of this component.
	 * May be freely modified in order to change the current target.
	 */
	public Vector3 getTarget() {
		return target;
	}

	/**
	 * Returns the maximum strength per component of the force vector to apply to reach the current target.
	 * May be freely modified in order to change the current strength.
	 */
	public Vector3 getStrength() {
		return strength;
	}

	/**
	 * Returns the distance around the current target to consider as having reached the target.
	 */
	public double getBuffer() {
		return buffer;
	}
	/**
	 * Sets the distance around the current target to consider as having reached the target to {@code buffer}.
	 */
	public void setBuffer(double buffer) {
		this.buffer = ArgVerify.greaterThanEqual("buffer", 0.0, buffer);
	}
}
