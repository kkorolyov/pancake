package dev.kkorolyov.pancake.component.movement;

import dev.kkorolyov.pancake.entity.Component;
import dev.kkorolyov.pancake.math.Vector;

/**
 * Net force acting on an entity.
 */
public class Force implements Component {
	private final Vector force = new Vector();
	private float mass, invMass;
	
	/**
	 * Constructs a new force.
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
	
	/** @return force in {@code N} */
	public Vector getForce() {
		return force;
	}

	/** @return mass in {@code kg} */
	public float getMass() {
		return mass;
	}
	/** @param mass new mass */
	public void setMass(float mass) {
		this.mass = mass;
		invMass = 1 / mass;
	}
}
