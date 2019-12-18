package dev.kkorolyov.pancake.core.component.movement;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.math.BoundedVector;
import dev.kkorolyov.pancake.platform.math.Vector;

/**
 * Damping applied on an entity.
 * Damping values are in the interval {@code [0, 1]}, essentially translating to {@code [immediate stop, no damping]}.
 */
public class Damping implements Component {
	private final Vector damping;

	/**
	 * Constructs a new damping component with equal damping across all axes.
	 * @param damping damping along all axes
	 */
	public Damping(double damping) {
		this.damping = new BoundedVector(Vector.all(damping), Vector.all(0), Vector.all(1));
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
	private static double damp(double velocity, double force, double damping) {
		return (velocity < 0 ? force >= 0 : force <= 0) ? damping : 1;
	}
	
	/** @return damping vector, constrained {@code [0, 1]} along all axes */
	public Vector getDamping() {
		return damping;
	}
}
