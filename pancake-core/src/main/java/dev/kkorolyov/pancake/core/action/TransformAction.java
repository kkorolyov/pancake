package dev.kkorolyov.pancake.core.action;

import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.math.Vector;

import java.util.Objects;

/**
 * Sets a transform.
 */
public class TransformAction extends Action {
	private final Vector position;
	private final Float rotation;

	/**
	 * Constructs a new transform action which only alters position.
	 * @see #TransformAction(Vector, Float)
	 */
	public TransformAction(Vector position) {
		this(position, null);
	}
	/**
	 * Constructs a new transform action.
	 * @param position position to set on accepted entities
	 * @param rotation rotation to set on accepted entities
	 */
	public TransformAction(Vector position, Float rotation) {
		super(Transform.class);

		this.position = position;
		this.rotation = rotation;
	}

	@Override
	protected void apply(Entity entity) {
		Transform entityTransform = entity.get(Transform.class);

		entityTransform.getPosition().set(position);
		if (rotation != null) entityTransform.setRotation(rotation);
	}

	/** @return position vector set on accepted entities */
	public Vector getPosition() {
		return position;
	}
	/** @return rotation set on accepted entities, or {@code null} */
	public Float getRotation() {
		return rotation;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || !getClass().isAssignableFrom(obj.getClass())) return false;

		TransformAction o = (TransformAction) obj;
		return Objects.equals(position, o.position)
				&& Objects.equals(rotation, o.rotation);
	}
	@Override
	public int hashCode() {
		return Objects.hash(position, rotation);
	}
}
