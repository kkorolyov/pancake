package dev.kkorolyov.pancake.core.component.limit;

import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.platform.math.Vector3;

/**
 * Constrains minimum and maximum transform offsets.
 */
public class TransformLimit implements Limit<Transform> {
	private final Vector3 translationMin = Vector3.observable(Vector3.of(), this::computeTransformMax);
	private final Vector3 translationMax = Vector3.observable(Vector3.of(), this::computeTransformMin);
	private final Vector3 scaleMin = Vector3.observable(Vector3.of(1, 1, 1), this::computeScaleMax);
	private final Vector3 scaleMax = Vector3.observable(Vector3.of(1, 1, 1), this::computeScaleMin);

	/**
	 * Returns the mutable translation minimum bound.
	 * When the vector is modified, updates {@link #getTranslationMax()} components as needed to keep them {@code >=} the return vector.
	 */
	public Vector3 getTranslationMin() {
		return translationMin;
	}
	/**
	 * Returns the mutable translation maximum bound.
	 * When the vector is modified, updates {@link #getTranslationMin()} components as needed to keep them {@code <=} the return vector.
	 */
	public Vector3 getTranslationMax() {
		return translationMax;
	}

	/**
	 * Returns the mutable scale minimum bound.
	 * When the vector is modified, updates {@link #getScaleMax()} components as needed to keep them {@code >=} the return vector.
	 */
	public Vector3 getScaleMin() {
		return scaleMin;
	}
	/**
	 * Returns the mutable scale maximum bound.
	 * When the vector is modified, updates {@link #getScaleMin()} components as needed to keep them {@code <=} the return vector.
	 */
	public Vector3 getScaleMax() {
		return scaleMax;
	}

	@Override
	public void limit(Transform component) {
		var translation = component.getTranslation();
		translation.setX(Math.min(translationMax.getX(), Math.max(translationMin.getX(), translation.getX())));
		translation.setY(Math.min(translationMax.getY(), Math.max(translationMin.getY(), translation.getY())));
		translation.setZ(Math.min(translationMax.getZ(), Math.max(translationMin.getZ(), translation.getZ())));

		var scale = component.getScale();
		scale.setX(Math.min(scaleMax.getX(), Math.max(scaleMin.getX(), scale.getX())));
		scale.setY(Math.min(scaleMax.getY(), Math.max(scaleMin.getY(), scale.getY())));
		scale.setZ(Math.min(scaleMax.getZ(), Math.max(scaleMin.getZ(), scale.getZ())));
	}

	private void computeTransformMin() {
		translationMin.setX(Math.min(translationMin.getX(), translationMax.getX()));
		translationMin.setY(Math.min(translationMin.getY(), translationMax.getY()));
		translationMin.setZ(Math.min(translationMin.getZ(), translationMax.getZ()));
	}
	private void computeTransformMax() {
		translationMax.setX(Math.max(translationMin.getX(), translationMax.getX()));
		translationMax.setY(Math.max(translationMin.getY(), translationMax.getY()));
		translationMax.setZ(Math.max(translationMin.getZ(), translationMax.getZ()));
	}
	private void computeScaleMin() {
		scaleMin.setX(Math.min(scaleMin.getX(), scaleMax.getX()));
		scaleMin.setY(Math.min(scaleMin.getY(), scaleMax.getY()));
		scaleMin.setZ(Math.min(scaleMin.getZ(), scaleMax.getZ()));
	}
	private void computeScaleMax() {
		scaleMax.setX(Math.max(scaleMin.getX(), scaleMax.getX()));
		scaleMax.setY(Math.max(scaleMin.getY(), scaleMax.getY()));
		scaleMax.setZ(Math.max(scaleMin.getZ(), scaleMax.getZ()));
	}
}
