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
												damping = new Vector(.9f, .9f, .9f);
	private float invMass;
	private float epsilon = .0001f;
	
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
		updateVelocity(dt);
	}
	private void updateVelocity(float dt) {
		damp();

		float vxOld = velocity.getX(),
					vyOld = velocity.getY(),
					vzOld = velocity.getZ();
		
		velocity.translate(	capSpeedChange(vxOld, maxSpeed.getX(), force.getX(), dt),
												capSpeedChange(vyOld, maxSpeed.getY(), force.getY(), dt),
												capSpeedChange(vzOld, maxSpeed.getZ(), force.getZ(), dt));
	}
	private float capSpeedChange(float velocity, float maxSpeed, float force, float dt) {
		if (force == 0)	// Skip computations
			return 0;
		
		float dv = (force * invMass) * dt;
		return (Math.abs(velocity + dv) > maxSpeed) ? 0 : dv;	// TODO Get to max speed
	}
	private void damp() {	// TODO Icky
		if (velocity.getX() != 0 && (force.getX() == 0 || opposite(velocity.getX(), force.getX())))
			velocity.setX(velocity.getX() * damping.getX());
		if (velocity.getY() != 0 && (force.getY() == 0 || opposite(velocity.getY(), force.getY())))
			velocity.setY(velocity.getY() * damping.getY());
		if (velocity.getZ() != 0 && (force.getZ() == 0 || opposite(velocity.getZ(), force.getZ())))
			velocity.setZ(velocity.getZ() * damping.getZ());
	}
	private static boolean opposite(float val1, float val2) {
		return (val1 < 0 && val2 >= 0) || (val2 < 0 && val1 >= 0);
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
