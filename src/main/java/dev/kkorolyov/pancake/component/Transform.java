package dev.kkorolyov.pancake.component;

import dev.kkorolyov.pancake.Component;
import dev.kkorolyov.pancake.math.Vector;

/**
 * Position and rotation of an entity local to a parent transform.
 */
public class Transform implements Component {
	private final Vector position;

	/**
	 * Constructs a new transform.
	 * @param position initial position
	 */
	public Transform(Vector position) {
		this.position = position;
	}
	
	/** @return local position relative to parent transform */
	public Vector getPosition() {
		return position;
	}
}
