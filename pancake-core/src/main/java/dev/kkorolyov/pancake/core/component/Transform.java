package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.math.Vector;

/**
 * Position and orientation of an entity local to a parent transform.
 */
public class Transform implements Component {
	private final Vector position;
	private final Vector globalPosition = new Vector();
	private final Vector orientation;
	private final Vector globalOrientation = new Vector();

	private Transform parent;
	private boolean rotatesWithParent;

	/**
	 * Constructs a new transform with orientation 0 rads about all axes.
	 * @see #Transform(Vector, Vector)
	 */
	public Transform(Vector position) {
		this(position, new Vector());
	}
	/**
	 * Constructs a transform with no parent.
	 * @see #Transform(Vector, Vector, Transform, boolean)
	 */
	public Transform(Vector position, Vector orientation) {
		this(position, orientation, null, false);
	}
	/**
	 * Constructs a new transform with a parent and orientation 0 rads about all axes.
	 * @see #Transform(Vector, Vector, Transform, boolean)
	 */
	public Transform(Vector position, Transform parent, boolean rotatesWithParent) {
		this(position, new Vector(), parent, rotatesWithParent);
	}
	/**
	 * Constructs a new transform.
	 * @param position initial position
	 * @param orientation vector where each component is the rotation in radians about the respective axis
	 * @param parent parent transform
	 * @param rotatesWithParent {@code true} means this transform is effectively "glued" to its position on {@code parent} and follows rotational position accordingly
	 */
	public Transform(Vector position, Vector orientation, Transform parent, boolean rotatesWithParent) {
		this.position = position;
		this.orientation = orientation.normalize();
		setParent(parent, rotatesWithParent);
	}
	
	/** @return position relative to parent transform */
	public Vector getPosition() {
		return position;
	}
	/** @return position relative to the root transform */
	public Vector getGlobalPosition() {
		globalPosition.set(position);

		if (parent != null) {
			if (rotatesWithParent)  {
				Vector orientation = parent.getGlobalOrientation();
				globalPosition.pivot(orientation.getZ(), orientation.getX());
			}
			globalPosition.add(parent.getGlobalPosition());
		} else {
			globalPosition.set(position);
		}
		return globalPosition;
	}

	/** @return orientation relative to parent */
	public Vector getOrientation() {
		return orientation;
	}
	/** @return orientation relative to the nearest ancestor which does not {@code rotatesWithParent} */
	public Vector getGlobalOrientation() {
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
