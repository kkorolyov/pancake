package dev.kkorolyov.pancake.core.component.movement;

import dev.kkorolyov.pancake.platform.entity.Component;
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
		this(Float.MAX_VALUE);
	}
	/**
	 * Constructs a new velocity.
	 * @param maxSpeed maximum speed along all axes
	 */
	public Velocity(float maxSpeed) {
		this(new Vector(maxSpeed, maxSpeed, maxSpeed));
	}
	/**
	 * Constructs a new velocity.
	 * @param maxSpeed vector defining maximum speed along each axis
	 */
	public Velocity(Vector maxSpeed) {
		this.maxSpeed = maxSpeed;
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
	private static float cap(float velocity, float maxSpeed) {
		return velocity < 0 ? Math.max(velocity, -maxSpeed) : Math.min(velocity, maxSpeed);
	}

	private Vector round() {
		velocity.set(round(velocity.getX()),
				round(velocity.getY()),
				round(velocity.getZ()));
		return velocity;
	}
	private static float round(float value) {
		return Float.compare(0, value) == 0 ? 0 : value;
	}

	/**
	 * Applies a positional change calculated from velocity and duration to a point.
	 * @param position position to move
	 * @param seconds seconds used in movement calculation
	 * @return {@code position} after movement applied
	 */
	public Vector move(Vector position, float seconds) {
		position.add(velocity, seconds);
		return position;
	}

	/** @return velocity vector */
	public Vector getVelocity() {
		return velocity;
	}

	/**
	 * If altering max speed values, it is recommended to avoid altering this vector directly, and instead use one of the {@link #setMaxSpeed(float)} methods, which constrain parameter values.
	 * @return maximum speed vector
	 * @see #setMaxSpeed(float, float, float)
	 */
	public Vector getMaxSpeed() {
		return maxSpeed;
	}

	/** @param maxSpeed new x, y, and z axes max speed */
	public void setMaxSpeed(float maxSpeed) {
		setMaxSpeed(maxSpeed, maxSpeed, maxSpeed);
	}
	/**
	 * Sets x and y axes values while retaining the z-axis value.
	 * @param vx new x-axis max speed
	 * @param vy new y-axis max speed
	 */
	public void setMaxSpeed(float vx, float vy) {
		setMaxSpeed(vx, vy, maxSpeed.getZ());
	}
	/**
	 * All values are constrained {@code >= 0}.
	 * @param vx new x-axis max speed
	 * @param vy new y-axis max speed
	 * @param vz new z-axis max speed
	 */
	public void setMaxSpeed(float vx, float vy, float vz) {
		maxSpeed.set(Math.abs(vx), Math.abs(vy), Math.abs(vz));
	}
}
