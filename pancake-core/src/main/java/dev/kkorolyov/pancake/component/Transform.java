package dev.kkorolyov.pancake.component;

import dev.kkorolyov.pancake.entity.Component;
import dev.kkorolyov.pancake.math.Vector;

/**
 * Position and rotation of an entity local to a parent transform.
 */
public class Transform implements Component {
	private final Vector position;
	private float rotation;

	/**
	 * Constructs a new transform with a rotation of {@code 0}.
	 * @param position initial position
	 */
	public Transform(Vector position) {
		this(position, 0);
	}
	/**
	 * Constructs a new transform.
	 * @param position initial position
	 * @param rotation degrees away from {@code x=0} line
	 */
	public Transform(Vector position, float rotation) {
		this.position = position;
		setRotation(rotation);
	}
	
	/** @return local position relative to parent transform */
	public Vector getPosition() {
		return position;
	}

	/** @return angle in degrees between this transform's facing and the {@code x=0} line, where a positive number means clockwise rotation and a negative means counterclockwise */
	public float getRotation() {
		return rotation;
	}
	/** @param rotation angle in degrees between this transform's facing and the {@code x=0} line */
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
}
