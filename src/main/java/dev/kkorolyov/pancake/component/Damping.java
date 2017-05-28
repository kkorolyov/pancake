package dev.kkorolyov.pancake.component;

import dev.kkorolyov.pancake.engine.Component;
import dev.kkorolyov.pancake.math.Vector;

/**
 * Damping applied on an entity.
 */
public class Damping implements Component {
	private final Vector damping = new Vector();
	
	/**
	 * Constructs a new {@code Damping} component with equal damping across all axes.
	 * @param damping damping along all axes
	 */
	public Damping(float damping) {
		setDamping(damping);
	}
	
	/**
	 * Damps a velocity vector along axes where applied force is zero or in the opposite direction of velocity.
	 * @param velocity velocity to damp
	 * @param force force applied 
	 * @return {@code velocity} after damping
	 */
	public Vector damp(Vector velocity, Vector force) {
		velocity.set(	velocity.getX() * damp(velocity.getX(), force.getX(), damping.getX()),
									velocity.getY() * damp(velocity.getY(), force.getY(), damping.getY()),
									velocity.getZ() * damp(velocity.getZ(), force.getZ(), damping.getZ()));
		return velocity;
	}
	private static float damp(float velocity, float force, float damping) {
		return (velocity < 0 ? force >= 0 : force <= 0) ? damping : 1;
	}
	
	/** @return damping vector */
	public Vector getDamping() {
		return damping;
	}
	
	/** @param damping new x, y, and z axes damping */
	public void setDamping(float damping) {
		setDamping(damping, damping, damping);
	}
	/**
	 * @param dx new x-axis damping
	 * @param dy new y-axis damping
	 */
	public void setDamping(float dx, float dy) {
		setDamping(dx, dy, damping.getZ());
	}
	/**
	 * @param dx new x-axis damping
	 * @param dy new y-axis damping
	 * @param dz new z-axis damping
	 */
	public void setDamping(float dx, float dy, float dz) {
		damping.set(Math.max(0, Math.min(1, dx)), Math.max(0, Math.min(1, dy)), Math.max(0, Math.min(1, dz)));
	}
	/** @param damping new damping vector */
	public void setDamping(Vector damping) {
		this.damping.set(damping);
	}
}
