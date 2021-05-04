package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.math.Vector1;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.math.Vectors;

/**
 * Position and orientation of an entity local to a parent transform.
 */
public final class Transform implements Component {
	private final Vector3 position;
	private final Vector3 globalPosition = Vectors.create(0, 0, 0);
	// TODO quaternions
	private final Vector1 orientation;
	private final Vector1 globalOrientation = Vectors.create(0);

	private Transform parent;
	private boolean rotatesWithParent;

	/**
	 * Constructs a new transform with orientation 0 rads.
	 * @see #Transform(Vector3, Vector1)
	 */
	public Transform(Vector3 position) {
		this(position, Vectors.create(0));
	}
	/**
	 * Constructs a transform with no parent.
	 * @see #Transform(Vector3, Vector1, Transform, boolean)
	 */
	public Transform(Vector3 position, Vector1 orientation) {
		this(position, orientation, null, false);
	}
	/**
	 * Constructs a new transform with a parent and orientation 0 rads about all axes.
	 * @see #Transform(Vector3, Vector1, Transform, boolean)
	 */
	public Transform(Vector3 position, Transform parent, boolean rotatesWithParent) {
		this(position, Vectors.create(0), parent, rotatesWithParent);
	}
	/**
	 * Constructs a new transform.
	 * @param position initial position
	 * @param orientation radians along x-y plane from the +x axis
	 * @param parent parent transform
	 * @param rotatesWithParent {@code true} means this transform is effectively "glued" to its position on {@code parent} and follows rotational position accordingly
	 */
	public Transform(Vector3 position, Vector1 orientation, Transform parent, boolean rotatesWithParent) {
		this.position = Vectors.create(position);
		this.orientation = Vectors.create(orientation);
		setParent(parent, rotatesWithParent);
	}

	/** @return position relative to parent transform */
	public Vector3 getPosition() {
		return position;
	}
	/** @return position relative to the root transform */
	public Vector3 getGlobalPosition() {
		globalPosition.set(position);

		if (parent != null) {
			if (rotatesWithParent) {
				double theta = parent.getGlobalOrientation().getX();

				globalPosition.setX(globalPosition.getX() * Math.cos(theta) - globalPosition.getY() * Math.sin(theta));
				globalPosition.setY(globalPosition.getX() * Math.sin(theta) + globalPosition.getY() * Math.cos(theta));
			}
			globalPosition.add(parent.getGlobalPosition());
		} else {
			globalPosition.set(position);
		}
		return globalPosition;
	}

	/** @return orientation relative to parent */
	public Vector1 getOrientation() {
		return orientation;
	}
	/** @return orientation relative to the nearest ancestor which does not {@code rotatesWithParent} */
	public Vector1 getGlobalOrientation() {
		globalOrientation.set(orientation);

		if (parent != null && rotatesWithParent) {
			globalOrientation.add(parent.getGlobalOrientation());
		}
		return globalOrientation;
	}

	/**
	 * @param parent parent to which this transform's position (and optionally, orientation) are relative to
	 * @param rotatesWithParent {@code true} means this transform's orientation is evaluated relative to {@code parent}
	 * @return {@code this}
	 */
	public Transform setParent(Transform parent, boolean rotatesWithParent) {
		this.parent = parent;
		this.rotatesWithParent = rotatesWithParent;

		return this;
	}
}
