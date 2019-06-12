package dev.kkorolyov.pancake.core.component.movement;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.math.BoundedVector;
import dev.kkorolyov.pancake.platform.math.Vector;

/**
 * Velocity of a moving entity.
 */
public class Velocity implements Component {
	private final Vector velocity = new Vector();
	private final Vector maxSpeed;

	/**
	 * Constructs a new velocity with effectively infinite max speed.
	 */
	public Velocity() {
		this(Double.MAX_VALUE);
	}
	/**
	 * Constructs a new velocity.
	 * @param maxSpeed maximum speed along all axes
	 */
	public Velocity(double maxSpeed) {
		this(new Vector(maxSpeed, maxSpeed, maxSpeed));
	}
	/**
	 * Constructs a new velocity.
	 * @param maxSpeed vector defining maximum speed along each axis
	 */
	public Velocity(Vector maxSpeed) {
		this.maxSpeed = new BoundedVector(maxSpeed, Vector.all(0), Vector.all(Double.MAX_VALUE));
	}

	/**
	 * Caps the speed along each axis such that it does not exceed max speed.
	 * @return velocity vector after capping applied
	 */
	public Vector cap() {
		round().set(cap(velocity.getX(), maxSpeed.getX()),
				cap(velocity.getY(), maxSpeed.getY()),
				cap(velocity.getZ(), maxSpeed.getZ()));
		return velocity;
	}
	private static double cap(double velocity, double maxSpeed) {
		return velocity < 0 ? Math.max(velocity, -maxSpeed) : Math.min(velocity, maxSpeed);
	}

	private Vector round() {
		velocity.set(round(velocity.getX()),
				round(velocity.getY()),
				round(velocity.getZ()));
		return velocity;
	}
	private static double round(double value) {
		return Double.compare(0, value) == 0 ? 0 : value;
	}

	/**
	 * Applies a positional change calculated from velocity and duration to a point.
	 * @param position position to move
	 * @param seconds seconds used in movement calculation
	 * @return {@code position} after movement applied
	 */
	public Vector move(Vector position, double seconds) {
		position.add(velocity, seconds);
		return position;
	}

	/** @return velocity vector */
	public Vector getVelocity() {
		return velocity;
	}
	/** @return maximum speed vector, constrained {@code > 0} along all axes */
	public Vector getMaxSpeed() {
		return maxSpeed;
	}
}
