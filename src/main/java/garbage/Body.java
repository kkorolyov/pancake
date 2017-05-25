package garbage;

import dev.kkorolyov.pancake.Component;
import dev.kkorolyov.pancake.math.Vector;

/**
 * Defines a body which reacts to physical forces.
 * <h5>A body is described by the following attributes:</h5>
 * <ul>
 * <li>Mass - A real number representing current mass in {@code kg}</li>
 * <li>Velocity - A 3D vector representing current velocity in {@code m/s}</li>
 * <li>Max Speed - A positive 3D vector representing maximum attainable speed in {@code m/s} along each axis</li>
 * <li>Force - A 3D vector representing current net force in {@code N}</li>
 * <li>Damping - A 3D vector with values within the range {@code [0, 1]} representing the proportion of velocity retained after each {@link #update(float)}</li>
 * </ul>
 * Because body attributes use floating-point values, a very small <b>epsilon</b> value is used in velocity calculations as a representation of absolute zero.
 */
public class Body implements Component {
	private static float epsilon = .001f;

	private final Vector 	velocity = new Vector(),
												collisionVelocity = new Vector(),
												maxSpeed = new Vector(5, 5, 5),
												force = new Vector(),
												damping = new Vector(.6f, .6f, .6f),
												effectiveDamping = new Vector();
	private float mass,
								invMass;
	private boolean collided = false;
	
	private static boolean isZero(float value) {
		return Math.abs(value) <= epsilon;
	}
	
	/** @return current "essentially zero" value for all bodies */
	public static float getEpsilon() {
		return epsilon;
	}
	/** @param epsilon new "essentially zero" value for all bodies */
	public static void setEpsilon(float epsilon) {
		Body.epsilon = epsilon;
	}
	
	/**
	 * Constructs a new body with a set mass.
	 * @param mass body mass
	 */
	public Body(float mass) {
		setMass(mass);
		setDamping(1);
	}
	
	/**
	 * Updates this body's velocity according to current applied forces.
	 * @param dt seconds "elapsed" during this update
	 */
	public void update(float dt) {
		if (collided) {
			velocity.set(collisionVelocity);
			collided = false;
		} else
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
		if (force == 0 || (velocity < 0 ? (velocity == -maxSpeed && force < 0) : (velocity == maxSpeed && force > 0)))	// Skip computations
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
	
	/**
	 * Simulates a collision between two bodies by applying the net force of each body to the other one.
	 * @param other body colliding with
	 */
	public void apply(Body other) {
		float massTotal = mass + other.mass;
		
		applyCollision(other.velocity, other.mass, massTotal, mass - other.mass);
		other.applyCollision(velocity, mass, massTotal, other.mass - mass);
	}
	private void applyCollision(Vector other, float otherMass, float massTotal, float massDiff) {
		collisionVelocity.set(velocity);
		
		collisionVelocity.scale(massDiff);
		collisionVelocity.add(other, 2 * otherMass);
		collisionVelocity.scale(1 / massTotal);
		
		collided = true;
	}
	
	/** @return this body's mass */
	public float getMass() {
		return 1 / invMass;
	}
	/** @param mass new mass */
	public void setMass(float mass) {
		this.mass = mass;
		invMass = 1 / mass;
	}
	
	@Override
	public String toString() {
		return toString(" ");
	}
	/** @return a string representation of this body's vectors separated by newlines */
	public String toStringMultiline() {
		return toString(System.lineSeparator());
	}
	private String toString(String delimeter) {
		return "v=" + velocity + delimeter + "max=" + maxSpeed + delimeter + "F=" + force + delimeter + "D=" + damping;
	}
}
