package dev.kkorolyov.pancake.entity;

import dev.kkorolyov.pancake.entity.collision.Bounds;
import dev.kkorolyov.pancake.entity.collision.Vector;

/**
 * Defines a body which reacts to physical forces.
 * The body's vector quantities are defined as {@code units/second}.
 */
public class Body {
	private final Vector 	velocity = new Vector(),
												maxSpeed = new Vector(5, 5, 5),
												force = new Vector(),
												damping = new Vector(.9f, .9f, .9f),
												effectiveDamping = new Vector();
	private float invMass;
	private float epsilon = (float) 1 / 1000;
	
	/**
	 * Constructs a new body with a set mass.
	 * @param mass body mass
	 */
	public Body(float mass) {
		setMass(mass);
	}
	
	/**
	 * Updates this body's velocity and acceleration.
	 * @param dt seconds "elapsed" during this update
	 */
	public void update(float dt) {
		damp();
		updateVelocity(dt);
		roundZero();
	}
	
	private void updateVelocity(float dt) {
		velocity.set(	capVelocity(velocity.getX(), maxSpeed.getX(), force.getX(), dt),
									capVelocity(velocity.getY(), maxSpeed.getY(), force.getY(), dt),
									capVelocity(velocity.getZ(), maxSpeed.getZ(), force.getZ(), dt));
	}
	private float capVelocity(float velocity, float maxSpeed, float force, float dt) {
		if (force == 0 || Math.abs(velocity) == maxSpeed)	// Skip computations
			return velocity;
		
		float dv = (force * invMass) * dt,
					newVelocity = velocity + dv;
		
		return (Math.abs(newVelocity) > maxSpeed) ? (newVelocity < 0 ? -maxSpeed : maxSpeed) : newVelocity;
	}
	
	private void damp() {
		effectiveDamping.setX(shouldDamp(velocity.getX(), force.getX()) ? damping.getX() : 1);
		effectiveDamping.setY(shouldDamp(velocity.getY(), force.getY()) ? damping.getY() : 1);
		effectiveDamping.setZ(shouldDamp(velocity.getZ(), force.getZ()) ? damping.getZ() : 1);
		
		velocity.scale(effectiveDamping);
	}
	private static boolean shouldDamp(float velocity, float force) {
		return velocity != 0 && (velocity < 0 ? force >= 0 : force <= 0);	// Non-zero velocity and opposite or zero force
	}
	
	private void roundZero() {
		if (isZero(velocity.getX()))
			velocity.setX(0);
		if (isZero(velocity.getY()))
			velocity.setY(0);
		if (isZero(velocity.getZ()))
			velocity.setZ(0);
	}
	private boolean isZero(float value) {
		return Math.abs(value) <= epsilon;
	}
	
	/**
	 * Applies this body's velocity to a bound's origin position.
	 * @param bounds bounds to apply velocity to
	 */
	public void apply(Bounds bounds) {
		bounds.getOrigin().add(velocity);
	}
	
	/** @return this body's velocity */
	public Vector getVelocity() {
		return velocity;
	}
	
	/**
	 * Set's this body's net force to {@code 0}.
	 */
	public void clearForce() {
		scaleForce(0);
	}
	/**
	 * Inverts this body's net force.
	 */
	public void invertForce() {
		scaleForce(-1);
	}
	/**
	 * Scales this body's net force.
	 * @param scale value to scale by
	 */
	public void scaleForce(float scale) {
		force.scale(scale);
	}
	
	/**
	 * Adds a force to this body's net force.
	 * @param fx added x-axis force
	 * @param fy added y-axis force
	 */
	public void addForce(float fx, float fy) {
		addForce(fx, fy, 0);
	}
	/**
	 * Adds a force to this body's net force.
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
	/** @param force new net force vector */
	public void setForce(Vector force) {
		this.force.set(force);
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
		damping.set(dx, dy, dz);
	}
	/** @param damping new damping vector */
	public void setDamping(Vector damping) {
		this.damping.set(damping);
	}
	
	/** @return this body's mass */
	public float getMass() {
		return 1 / invMass;
	}
	/** @param mass new mass */
	public void setMass(float mass) {
		invMass = 1 / mass;
	}
	
	@Override
	public String toString() {
		return toString(" ");
	}
	/** @return a string representation of this body's vectors seperated by newlines */
	public String toStringMultiline() {
		return toString(System.lineSeparator());
	}
	private String toString(String delimeter) {
		return "v=" + velocity + delimeter + "max=" + maxSpeed + delimeter + "F=" + force + delimeter + "D=" + damping;
	}
}
