package dev.kkorolyov.pancake.component.movement;

import dev.kkorolyov.pancake.entity.Component;
import dev.kkorolyov.pancake.math.Vector;

/**
 * Maximum speed attainable by a moving entity.
 */
public class MaxSpeed implements Component {
	private final Vector maxSpeed = new Vector();

	/**
	 * Constructs a new max speed component with equal max speed across all axes.
	 * @param maxSpeed maximum speed along all axes
	 */
	public MaxSpeed(float maxSpeed) {
		setMaxSpeed(maxSpeed);
	}

	/**
	 * Caps a velocity vector so that its speed along each axis does not exceed the max speed defined by this component.
	 * @param velocity capped velocity
	 * @return {@code velocity} after capping applied
	 */
	public Vector cap(Vector velocity) {
		velocity.set(cap(velocity.getX(), maxSpeed.getX()),
				cap(velocity.getY(), maxSpeed.getY()),
				cap(velocity.getZ(), maxSpeed.getZ()));
		return velocity;
	}
	private static float cap(float velocity, float maxSpeed) {
		return velocity < 0 ? Math.max(velocity, -maxSpeed) : Math.min(velocity, maxSpeed);
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
	 * All values are constrained to be {@code > 0}.
	 * @param vx new x-axis max speed
	 * @param vy new y-axis max speed
	 * @param vz new z-axis max speed
	 */
	public void setMaxSpeed(float vx, float vy, float vz) {
		maxSpeed.set(Math.abs(vx), Math.abs(vy), Math.abs(vz));
	}
}
