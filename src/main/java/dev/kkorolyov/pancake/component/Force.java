package dev.kkorolyov.pancake.component;

import dev.kkorolyov.pancake.core.Component;
import dev.kkorolyov.pancake.math.Vector;

/**
 * Net force acting on an entity.
 */
public class Force implements Component {
	private final Vector force = new Vector();
	private float mass, invMass;
	
	/**
	 * Constructs a new {@code Force} component with a set mass.
	 * @param mass mass in {@code kg}
	 */
	public Force(float mass) {
		setMass(mass);
	}
	
	/**
	 * Applies an acceleration calculated from net force, mass, and duration to a velocity vector.
	 * @param velocity velocity to accelerate
	 * @param seconds seconds used in acceleration calculation
	 * @return {@code velocity} after acceleration
	 */
	public Vector accelerate(Vector velocity, float seconds) {
		velocity.add(force, invMass * seconds);	// a = f/m, v' = v + at
		return velocity;
	}
	
	/**
	 * Set's this force to {@code 0}.
	 */
	public void clearForce() {
		scaleForce(0);
	}
	/**
	 * Inverts this force.
	 */
	public void invertForce() {
		scaleForce(-1);
	}
	/**
	 * Scales this force.
	 * @param scale value to scale by
	 */
	public void scaleForce(float scale) {
		force.scale(scale);
	}
	
	/**
	 * Adds a force to this force.
	 * @param fx added x-axis force
	 * @param fy added y-axis force
	 */
	public void addForce(float fx, float fy) {
		addForce(fx, fy, 0);
	}
	/**
	 * Adds a force to this force.
	 * @param fx added x-axis force
	 * @param fy added y-axis force
	 * @param fz added z-axis force
	 */
	public void addForce(float fx, float fy, float fz) {
		force.translate(fx, fy, fz);
	}
	/**
	 * Adds a force to this body's net force.
	 * @param force added force vector
	 */
	public void addForce(Vector force) {
		this.force.add(force);
	}
	
	/** @return force vector */
	public Vector getForce() {
		return force;
	}
	
	/**
	 * @param fx new x-axis force
	 * @param fy new y-axis force
	 */
	public void setForce(float fx, float fy) {
		setForce(fx, fy, force.getZ());
	}
	/**
	 * @param fx new x-axis force
	 * @param fy new y-axis force
	 * @param fz new z-axis force
	 */
	public void setForce(float fx, float fy, float fz) {
		force.set(fx, fy, fz);
	}
	/** @param force new force vector */
	public void setForce(Vector force) {
		this.force.set(force);
	}
	
	/** @return this body's mass */
	public float getMass() {
		return mass;
	}
	/** @param mass new mass */
	public void setMass(float mass) {
		this.mass = mass;
		invMass = 1 / mass;
	}
}
