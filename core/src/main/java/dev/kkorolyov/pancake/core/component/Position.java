package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.math.Vector3;

/**
 * Placement of an entity in space.
 * May optionally be relative to a parent position.
 */
public final class Position implements Component {
	private final Vector3 value;

	private Position parent;

	private final ThreadLocal<Vector3> tGlobalValue = ThreadLocal.withInitial(Vector3::of);

	/**
	 * Constructs a new position with initial {@code value}.
	 */
	public Position(Vector3 value) {
		this.value = Vector3.of(value);
	}

	/**
	 * Returns the position vector relative to the parent position.
	 * If this component has no parent, this is the same as {@link #getGlobalValue()}.
	 */
	public Vector3 getValue() {
		return value;
	}
	/**
	 * Returns the position vector in global space.
	 * The global position is calculated simply by summing the local position of this component and the global position of its parent - does not consider the orientation of the parent.
	 * If this component has no parent, this is the same as {@link #getValue()}.
	 */
	public Vector3 getGlobalValue() {
		Vector3 globalValue = tGlobalValue.get();
		globalValue.set(value);

		if (parent != null) {
			globalValue.add(parent.getGlobalValue());
		}

		return globalValue;
	}

	/**
	 * Returns the parent position, if any;
	 */
	public Position getParent() {
		return parent;
	}
	/**
	 * Sets parent position to {@code parent}.
	 * Setting to {@code null} makes this a root position.
	 */
	public void setParent(Position parent) {
		this.parent = parent;
	}
}
