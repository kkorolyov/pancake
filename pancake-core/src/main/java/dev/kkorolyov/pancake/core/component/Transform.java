package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.math.Vector;

/**
 * Position and rotation of an entity local to a parent transform.
 */
public class Transform implements Component {
	private final Vector position;
	private final Vector globalPosition = new Vector();
	private float rotation;	// TODO 2D rotation or 3D direction vector

	private Transform parent;
	private boolean rotatesWithParent;

	/**
	 * Constructs a new transform with {@code 0} rotation.
	 * @see #Transform(Vector, float)
	 */
	public Transform(Vector position) {
		this(position, 0);
	}
	/**
	 * Constructs a transform with no parent.
	 * @see #Transform(Vector, float, Transform, boolean)
	 */
	public Transform(Vector position, float rotation) {
		this(position, rotation, null, false);
	}
	/**
	 * Constructs a new transform.
	 * @param position initial position
	 * @param rotation radians away from local {@code x=0} line, + is counterclockwise, - is clockwise
	 * @param parent parent transform
	 * @param rotatesWithParent {@code true} means this transform is effectively "glued" to its position on {@code parent} and follows rotational position accordingly
	 */
	public Transform(Vector position, float rotation, Transform parent, boolean rotatesWithParent) {
		this.position = position;
		setRotation(rotation);
		setParent(parent, rotatesWithParent);
	}
	
	/** @return position relative to parent transform */
	public Vector getPosition() {
		return position;
	}

	/** @return position relative to the root transform */
	public Vector getGlobalPosition() {
		if (parent != null) {
			globalPosition.set(position);
			if (rotatesWithParent)  {
				globalPosition.rotate(parent.getGlobalRotation(), 0);	// TODO Phi
			}
			globalPosition.add(parent.getGlobalPosition());
		} else {
			globalPosition.set(position);
		}
		return globalPosition;
	}

	/** @return relative to parent, radians between this transform's facing and the {@code x=0} line, where a positive number means counterclockwise rotation and a negative means clockwise */
	public float getRotation() {
		return rotation;
	}
	/** @param rotation new radians between this transform's facing and the {@code x=0} line, relative to parent */
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	/** @return rotation relative to the nearest ancestor which does not {@code rotatesWithParent} */
	public float getGlobalRotation() {
		return (parent != null && rotatesWithParent)
				? rotation + parent.getGlobalRotation()
				: rotation;
	}

	/**
	 * @param parent parent to which this transform's position (and optionally, rotation) are relative to
	 * @param rotatesWithParent {@code true} means this transform's rotation is evaluated relative to {@code parent}
	 * @return {@code this}
	 */
	public Transform setParent(Transform parent, boolean rotatesWithParent) {
		this.parent = parent;
		this.rotatesWithParent = rotatesWithParent;

		return this;
	}
}
