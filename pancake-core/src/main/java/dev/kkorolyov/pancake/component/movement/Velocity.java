package dev.kkorolyov.pancake.component.movement;

import dev.kkorolyov.pancake.entity.Component;
import dev.kkorolyov.pancake.math.Vector;

import static dev.kkorolyov.pancake.Config.config;

/**
 * Velocity of a moving entity.
 */
public class Velocity implements Component {
	private static final float ZERO = Float.parseFloat(config.get("zero"));
	private final Vector velocity = new Vector();

	/**
	 * Sets all axis values which are "effectively zero" to zero.
	 * The effective zero value is specified by the {@code zero} configuration property.
	 * @return velocity after rounding applied
	 */
	public Vector round() {
		velocity.set(round(velocity.getX()),
				round(velocity.getY()),
				round(velocity.getZ()));
		return velocity;
	}
	private static float round(float value) {
		return Math.abs(value) <= ZERO ? 0 : value;
	}

	/**
	 * Applies a positional change calculated from velocity and duration to a point.
	 * @param position position to move
	 * @param seconds seconds used in movement calculation
	 * @return {@code position} after movement applied
	 */
	public Vector move(Vector position, float seconds) {
		position.add(velocity, seconds);
		return position;
	}

	/** @return velocity vector */
	public Vector getVelocity() {
		return velocity;
	}
}
