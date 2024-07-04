package dev.kkorolyov.pancake.core.animation;

import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.platform.animation.Frame;
import dev.kkorolyov.pancake.platform.math.Vector3;

import java.util.Objects;

/**
 * A frame of an animation over a {@code Transform}.
 * All 3 components (TRS) are encoded as axis-aligned deltas from a base state of `(0, 0, 0)`.
 */
public final class TransformFrame implements Frame<TransformFrame> {
	private static final Vector3 X_AXIS = Vector3.of(1);
	private static final Vector3 Y_AXIS = Vector3.of(0, 1);
	private static final Vector3 Z_AXIS = Vector3.of(0, 0, 1);

	private final Vector3 translation, rotation, scale;

	/**
	 * Shortcut constructor for a frame with all {@code 0} vectors.
	 */
	public TransformFrame() {
		translation = Vector3.of();
		rotation = Vector3.of();
		scale = Vector3.of();
	}
	public TransformFrame(Vector3 translation, Vector3 rotation, Vector3 scale) {
		this.translation = Vector3.of(translation);
		this.rotation = Vector3.of(rotation);
		this.scale = Vector3.of(scale);
	}

	private static Vector3 lerpVector(Vector3 a, Vector3 b, double mix) {
		return Vector3.of(lerpDouble(a.getX(), b.getX(), mix), lerpDouble(a.getY(), b.getY(), mix), lerpDouble(a.getZ(), b.getZ(), mix));
	}
	private static Double lerpDouble(double a, double b, double mix) {
		return a + (b - a) * mix;
	}

	/**
	 * Updates {@code transform} by this frame.
	 */
	public void apply(Transform transform) {
		transform.getTranslation().add(translation);

		transform.getRotation().rotate(rotation.getX(), X_AXIS);
		transform.getRotation().rotate(rotation.getY(), Y_AXIS);
		transform.getRotation().rotate(rotation.getZ(), Z_AXIS);

		transform.getScale().add(scale);
	}

	public Vector3 getTranslation() {
		return translation;
	}
	public Vector3 getRotation() {
		return rotation;
	}
	public Vector3 getScale() {
		return scale;
	}

	@Override
	public TransformFrame lerp(TransformFrame other, double mix) {
		return new TransformFrame(
				lerpVector(translation, other.translation, mix),
				lerpVector(rotation, other.rotation, mix),
				lerpVector(scale, other.scale, mix)
		);
	}
	@Override
	public TransformFrame sum(TransformFrame other) {
		var newTranslation = Vector3.of(translation);
		newTranslation.add(other.translation);
		var newRotation = Vector3.of(rotation);
		newRotation.add(other.rotation);
		var newScale = Vector3.of(scale);
		newScale.add(other.scale);

		return new TransformFrame(newTranslation, newRotation, newScale);
	}
	@Override
	public TransformFrame diff(TransformFrame other) {
		var newTranslation = Vector3.of(translation);
		newTranslation.add(other.translation, -1);
		var newRotation = Vector3.of(rotation);
		newRotation.add(other.rotation, -1);
		var newScale = Vector3.of(scale);
		newScale.add(other.scale, -1);

		return new TransformFrame(newTranslation, newRotation, newScale);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TransformFrame obj = (TransformFrame) o;
		return Objects.equals(translation, obj.translation) && Objects.equals(rotation, obj.rotation) && Objects.equals(scale, obj.scale);
	}
	@Override
	public int hashCode() {
		return Objects.hash(translation, rotation, scale);
	}
}
