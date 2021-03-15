package dev.kkorolyov.pancake.core.component.movement;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.math.Vector;

/**
 * Net force acting on an entity.
 */
public final class Force implements Component {
	private final Vector force = new Vector();
	private double mass, invMass;

	/**
	 * Constructs a new force.
	 * @param mass mass in {@code kg}
	 */
	public Force(double mass) {
		setMass(mass);
	}
	
	/**
	 * Applies an acceleration calculated from net force, mass, and duration to a velocity vector.
	 * @param velocity velocity to accelerate
	 * @param seconds seconds used in acceleration calculation
	 * @return {@code velocity} after acceleration
	 */
	public Vector accelerate(Vector velocity, double seconds) {
		velocity.add(force, invMass * seconds);	// a = f/m, v' = v + at
		return velocity;
	}
	
	/** @return force in {@code N} */
	public Vector getForce() {
		return force;
	}

	/** @return mass in {@code kg} */
	public double getMass() {
		return mass;
	}
	/** @param mass new mass */
	public void setMass(double mass) {
		this.mass = mass;
		invMass = 1 / mass;
	}
}
