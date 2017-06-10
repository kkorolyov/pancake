package dev.kkorolyov.pancake.component.movement;

import dev.kkorolyov.pancake.entity.Component;
import dev.kkorolyov.pancake.math.Vector;

/**
 * Maximum speed attainable by a moving entity.
 */
public class MaxSpeed implements Component {
	private final Vector maxSpeed = new Vector();
	
	/**
	 * Constructs a new {@code MaxSpeed} component with equal max speed across all axes.
	 * @param maxSpeed maximum speed along all axes
	 */
	public MaxSpeed(float maxSpeed) {
		setMaxSpeed(maxSpeed);
	}
	
	/**
	 * Caps a velocity vector so that its speed along each axis does not exceed the max speed defined by this component.
	 * @param velocity velocity to cap
	 * @return {@code velocity} after capping
	 */
	public Vector cap(Vector velocity) {
		velocity.set(	cap(velocity.getX(), maxSpeed.getX()),
									cap(velocity.getY(), maxSpeed.getY()),
									cap(velocity.getZ(), maxSpeed.getZ()));
		return velocity;
	}
	private static float cap(float velocity, float maxSpeed) {
		return velocity < 0 ? Math.max(velocity, -maxSpeed) : Math.min(velocity, maxSpeed);
	}
	
	/** @return maximum speed vector */
	public Vector getMaxSpeed() {
		return maxSpeed;
	}
	
	/** @param maxSpeed new x, y, and z axes max speed */
	public void setMaxSpeed(float maxSpeed) {
		setMaxSpeed(maxSpeed, maxSpeed, maxSpeed);
	}
	/**
	 * @param vx new x-axis max speed
	 * @param vy new y-axis max speed
	 */
	public void setMaxSpeed(float vx, float vy) {
		setMaxSpeed(vx, vy, maxSpeed.getZ());
	}
	/**
	 * @param vx new x-axis max speed
	 * @param vy new y-axis max speed
	 * @param vz new z-axis max speed
	 */
	public void setMaxSpeed(float vx, float vy, float vz) {
		maxSpeed.set(Math.abs(vx), Math.abs(vy), Math.abs(vz));
	}
	/** @param maxSpeed new max speed */
	public void setMaxSpeed(Vector maxSpeed) {
		this.maxSpeed.set(maxSpeed);
	}
}
