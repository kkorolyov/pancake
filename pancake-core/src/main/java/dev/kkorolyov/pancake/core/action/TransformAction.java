package dev.kkorolyov.pancake.core.action;

import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.math.Vector1;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.math.Vectors;

import java.util.Objects;

/**
 * Sets a transform.
 */
public class TransformAction implements Action {
	private final Vector3 position;
	private final Vector1 rotation;

	/**
	 * Constructs a new transform action which only alters position.
	 * @see #TransformAction(Vector3, Vector1)
	 */
	public TransformAction(Vector3 position) {
		this(position, null);
	}
	/**
	 * Constructs a new transform action.
	 * @param position position to set on accepted entities
	 * @param rotation rotation to set on accepted entities
	 */
	public TransformAction(Vector3 position, Vector1 rotation) {
		this.position = Vectors.create(position);
		this.rotation = rotation != null ? Vectors.create(rotation) : null;
	}

	@Override
	public void apply(Entity entity) {
		Transform entityTransform = entity.get(Transform.class);

		entityTransform.getPosition().set(position);
		if (rotation != null) entityTransform.getOrientation().set(rotation);
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

	@Override
	public String toString() {
		return "TransformAction{" +
				"position=" + position +
				", rotation=" + rotation +
				'}';
	}
}
