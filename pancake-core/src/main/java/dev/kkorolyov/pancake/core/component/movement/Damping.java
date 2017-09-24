package dev.kkorolyov.pancake.core.component.movement;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.math.Vector;

/**
 * Damping applied on an entity.
 * Damping values are in the interval {@code [0, 1]}, essentially translating to {@code [immediate stop, no damping]}.
 */
public class Damping implements Component {
	private final Vector damping = new Vector();

	/**
	 * Constructs a new damping component with equal damping across all axes.
	 * @param damping damping along all axes
	 */
	public Damping(float damping) {
		setDamping(damping);
	}
	
	/**
	 * Damps a velocity vector along axes where applied force is zero or in the opposite direction of velocity.
	 * Damping is done by multiplying a velocity component by the respective damping component, so, the smaller the damping value, the greater the decrease in velocity.
	 * @param velocity damped velocity
	 * @param force applied force
	 * @return {@code velocity} after damping applied
	 */
	public Vector damp(Vector velocity, Vector force) {
		velocity.set(velocity.getX() * damp(velocity.getX(), force.getX(), damping.getX()),
				velocity.getY() * damp(velocity.getY(), force.getY(), damping.getY()),
				velocity.getZ() * damp(velocity.getZ(), force.getZ(), damping.getZ()));
		return velocity;
	}
	private static float damp(float velocity, float force, float damping) {
		return (velocity < 0 ? force >= 0 : force <= 0) ? damping : 1;
	}
	
	/**
	 * If altering damping values, it is recommended to avoid altering this vector directly, and instead use one of the {@link #setDamping(float)} methods, which constrain parameter values.
	 * @return damping vector
	 * @see #setDamping(float, float, float)
	 */
	public Vector getDamping() {
		return damping;
	}
	
	/** @param damping new x, y, and z axes damping */
	public void setDamping(float damping) {
		setDamping(damping, damping, damping);
	}
	/**
	 * Sets x and y axes values while retaining the z-axis value.
	 * @param dx new x-axis damping
	 * @param dy new y-axis damping
	 */
	public void setDamping(float dx, float dy) {
		setDamping(dx, dy, damping.getZ());
	}
	/**
	 * All values are constrained in the interval {@code [0, 1]}.
	 * @param dx new x-axis damping
	 * @param dy new y-axis damping
	 * @param dz new z-axis damping
	 */
	public void setDamping(float dx, float dy, float dz) {
		damping.set(constrain(dx), constrain(dy), constrain(dz));
	}

	private float constrain(float value) {
		return Math.max(0, Math.min(1, value));
	}
}
