package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.math.Vector3;

/**
 * Facing of an entity in space.
 * May optionally be relative to a parent orientation.
 */
public final class Orientation implements Component {
	private final Vector3 value;

	private Orientation parent;

	/**
	 * Constructs a new orientation with initial {@code value}.
	 */
	public Orientation(Vector3 value) {
		this.value = Vector3.of(value);
		this.value.normalize();
	}

	/**
	 * Returns the unit vector representing where this component faces relative to its parent orientation.
	 */
	public Vector3 getValue() {
		return value;
	}

	/**
	 * Returns the parent orientation, if any.
	 */
	public Orientation getParent() {
		return parent;
	}
	/**
	 * Sets the parent orientation to {@code parent}.
	 * Setting to {@code null} makes this a root orientation.
	 */
	public void setParent(Orientation parent) {
		this.parent = parent;
	}
}
