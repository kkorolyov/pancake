package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.math.Matrix4;
import dev.kkorolyov.pancake.platform.math.Vector3;

/**
 * Translation, rotation, and scale of an entity in space.
 * All operations are relative to the transform's parent.
 * To perform calculations using this transform (e.g. physics-based collisions), take a vector relative to this transform (likely {@code (0, 0, 0)}) and apply this transform's matrix to it.
 */
public class Transform implements Component {
	private static final ThreadLocal<Matrix4> tCalcMatrix = ThreadLocal.withInitial(Matrix4::identity);
	private static final ThreadLocal<Matrix4> tReturnMatrix = ThreadLocal.withInitial(Matrix4::identity);

	private final Vector3 translation;
	private final Matrix4 rotation;
	private final Vector3 scale;

	private final Transform parent;

	/**
	 * Constructs a new transform with no parent.
	 * @see #Transform(Transform)
	 */
	public Transform() {
		this(null);
	}
	/**
	 * Constructs a new transform initialized to no translation, no rotation, a scale of {@code 1}, relative to {@code parent} transform.
	 */
	public Transform(Transform parent) {
		translation = Vector3.of();
		rotation = Matrix4.identity();
		scale = Vector3.of(1, 1, 1);

		this.parent = parent;
	}

	/**
	 * Returns the mutable translation vector relative to .
	 */
	public Vector3 getTranslation() {
		return translation;
	}
	/**
	 * Returns the mutable rotation matrix.
	 */
	public Matrix4 getRotation() {
		return rotation;
	}
	/**
	 * Returns the mutable scale vector.
	 */
	public Vector3 getScale() {
		return scale;
	}

	/**
	 * Returns the parent transform, if any.
	 * No parent implies that this transform resides at the top-level - or root - coordinate space.
	 */
	public Transform getParent() {
		return parent;
	}

	/**
	 * Returns the matrix for transforming local, transform-relative vectors to this transform's coordinates in root space.
	 */
	public Matrix4 getMatrix() {
		if (parent != null) {
			var parentMatrix = tCalcMatrix.get();
			parentMatrix.reset();
			parentMatrix.set(parent.getMatrix());

			var fullMatrix = tReturnMatrix.get();
			fullMatrix.reset();

			fullMatrix.multiply(parentMatrix);
			fullMatrix.multiply(buildLocalMatrix());

			return fullMatrix;
		} else {
			return buildLocalMatrix();
		}
	}
	private Matrix4 buildLocalMatrix() {
		var matrix = tCalcMatrix.get();
		matrix.reset();

		matrix.translate(translation);
		matrix.multiply(rotation);
		matrix.scale(scale);

		return matrix;
	}
}
