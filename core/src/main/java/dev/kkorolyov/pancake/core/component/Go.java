package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.utility.ArgVerify;

/**
 * Ephemeral marker of where an entity wants to go and how intent it is on getting there.
 */
public final class Go implements Component {
	private final Vector3 target;
	private double strength;
	private double buffer;

	/**
	 * Constructs a new go component with position {@code target}, {@code strength} of force to apply, and {@code buffer} distance around target considered as reached.
	 */
	public Go(Vector3 target, double strength, double buffer) {
		this.target = target;
		setStrength(strength);
		setBuffer(buffer);
	}

	/**
	 * Returns the mutable intended position.
	 */
	public Vector3 getTarget() {
		return target;
	}

	/**
	 * Returns the intended force magnitude to get to the target.
	 */
	public double getStrength() {
		return strength;
	}
	/**
	 * Sets the intended force magnitude to get to the target to {@code strength}.
	 */
	public void setStrength(double strength) {
		this.strength = ArgVerify.greaterThanEqual("strength", 0.0, strength);
	}

	/**
	 * Returns the distance around the target to consider as having reached the target.
	 */
	public double getBuffer() {
		return buffer;
	}
	/**
	 * Sets the distance around the target to consider as having reached the target to {@code buffer}.
	 */
	public void setBuffer(double buffer) {
		this.buffer = ArgVerify.greaterThanEqual("buffer", 0.0, buffer);
	}
}
