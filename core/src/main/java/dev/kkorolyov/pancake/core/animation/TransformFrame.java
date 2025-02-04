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

	private final Vector3 translation = Vector3.of();
	private final Vector3 rotation = Vector3.of();
	private final Vector3 scale = Vector3.of();

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
		var result = new TransformFrame();
		result.translation.set(lerpVector(other.translation, translation, mix));
		result.rotation.set(lerpVector(other.rotation, rotation, mix));
		result.scale.set(lerpVector(other.scale, scale, mix));

		return result;
	}
	@Override
	public TransformFrame sum(TransformFrame other) {
		var result = new TransformFrame();
		result.translation.set(translation);
		result.translation.add(other.translation);
		result.rotation.set(rotation);
		result.rotation.add(other.rotation);
		result.scale.set(scale);
		result.scale.add(other.scale);

		return result;
	}
	@Override
	public TransformFrame diff(TransformFrame other) {
		var result = new TransformFrame();
		result.translation.set(translation);
		result.translation.add(other.translation, -1);
		result.rotation.set(rotation);
		result.rotation.add(other.rotation, -1);
		result.scale.set(scale);
		result.scale.add(other.scale, -1);

		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof TransformFrame other)) return false;
		return Objects.equals(translation, other.translation) && Objects.equals(rotation, other.rotation) && Objects.equals(scale, other.scale);
	}
	@Override
	public int hashCode() {
		return Objects.hash(translation, rotation, scale);
	}
}
