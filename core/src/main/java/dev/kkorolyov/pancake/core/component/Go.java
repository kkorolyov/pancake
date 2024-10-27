package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.utility.ArgVerify;

// TODO similar use case to an Animator - can Go be described through a Transform or Force animation?
// doing so could also leverage the natural animation timeout to avoid the locked input issue with a Go component that can't reach its target
/**
 * Ephemeral marker of where an entity wants to go and how intent it is on getting there.
 */
public final class Go implements Component {
	private final Vector3 target;
	private double strength;
	private double proximity;
	private boolean snap;

	/**
	 * Constructs a new go component with position {@code target}, {@code strength} of force to apply, {@code proximity} radius around target considered as this component's entity having reached its target, and whether to {@code snap} position within proximity of target.
	 */
	public Go(Vector3 target, double strength, double proximity, boolean snap) {
		this.target = target;
		setStrength(strength);
		setProximity(proximity);
		this.snap = snap;
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
	 * Returns the radius around the target to consider as having reached the target.
	 */
	public double getProximity() {
		return proximity;
	}
	/**
	 * Sets the radius around the target to consider as having reached the target to {@code buffer}.
	 */
	public void setProximity(double proximity) {
		this.proximity = ArgVerify.greaterThanEqual("radius", 0.0, proximity);
	}

	/**
	 * Returns whether the owning entity's position should be set to exactly this component's target once it is within proximity.
	 */
	public boolean isSnap() {
		return snap;
	}
	/**
	 * Sets whether the owning entity's position should be set to exactly this component's target once it is within proximity to {@code snap}.
	 */
	public void setSnap(boolean snap) {
		this.snap = snap;
	}
}
