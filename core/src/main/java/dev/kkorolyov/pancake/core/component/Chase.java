package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.utility.ArgVerify;

/**
 * An intended position and maximum strength used to reach that position.
 * Represents a location an entity wants to get to.
 */
public final class Chase implements Component {
	private final Vector3 target;
	private double strength;
	private double buffer;

	private final ThreadLocal<Vector3> tApply = ThreadLocal.withInitial(Vector3::of);

	/**
	 * Constructs a new chase component with position {@code target}, {@code strength} of force to apply, and {@code buffer} distance around target considered as reached.
	 */
	public Chase(Vector3 target, double strength, double buffer) {
		this.target = Vector3.of(target);
		setStrength(strength);
		setBuffer(buffer);
	}

	/**
	 * Sets strength of a force vector along axes where the current force is less than the chase strength in the direction of the chase target.
	 */
	public void chase(Vector3 position, Vector3 force) {
		Vector3 apply = tApply.get();
		apply.set(target);
		apply.add(position, -1);

		if (Vector3.magnitude(apply) > buffer) {
			apply.normalize();
			apply.scale(strength);
			force.set(apply);
		}
	}

	/**
	 * Returns the position target of this component.
	 * May be freely modified in order to change the current target.
	 */
	public Vector3 getTarget() {
		return target;
	}

	/**
	 * Returns the magnitude applied to a force vector to reach the current target.
	 */
	public double getStrength() {
		return strength;
	}
	/**
	 * Sets the magnitude applied to a force vector to reach the current target to {@code strength}.
	 */
	public void setStrength(double strength) {
		this.strength = ArgVerify.greaterThanEqual("strength", 0.0, strength);
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
