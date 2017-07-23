package dev.kkorolyov.pancake.component.collision;

import dev.kkorolyov.pancake.entity.Component;
import dev.kkorolyov.pancake.math.Vector;

/**
 * Bounds defined by at least one of:
 * <pre>
 *   - Box
 *   - Sphere
 * </pre>
 */
public class Bounds implements Component {
	private Vector box;
	private Float radius;

	/**
	 * Constructs new bounds defined solely by a box.
	 * @see #Bounds(Vector, Float)
	 */
	public Bounds(Vector box) {
		this(box, null);
	}
	/**
	 * Constructs new bounds defined solely by a sphere.
	 * @see #Bounds(Vector, Float)
	 */
	public Bounds(Float radius) {
		this(null, radius);
	}
	/**
	 * Constructs new bounds.
	 * @param box box dimensions
	 * @param radius sphere radius
	 * @throws IllegalStateException if both {@code box} and {@code radius} are {@code null}
	 */
	public Bounds(Vector box, Float radius) {
		this.box = box;
		this.radius = radius;

		verifyDefined();
	}

	/** @return box dimensions, or {@code null} if not set */
	public Vector getBox() {
		return box;
	}
	/**
	 * @param box new box dimensions, {@code null} clears current value
	 * @return {@code this}
	 * @throws IllegalStateException if both {@code box} and {@code radius} of this instance are {@code null}
	 */
	public Bounds setBox(Vector box) {
		this.box = box;
		verifyDefined();

		return this;
	}

	/** @return sphere radius, or {@code null} if not set */
	public Float getRadius() {
		return radius;
	}
	/**
	 * @param radius new sphere radius, {@code null} clears current value
	 * @return {@code this}
	 * @throws IllegalStateException if both {@code box} and {@code radius} of this instance are {@code null}
	 */
	public Bounds setRadius(Float radius) {
		this.radius = radius;
		verifyDefined();

		return this;
	}

	private void verifyDefined() {
		if (box == null && radius == null) {
			throw new IllegalStateException(this + " is defined by neither a box nor a sphere");
		}
	}

	@Override
	public String toString() {
		return "Bounds{" +
				"box=" + box +
				", radius=" + radius +
				'}';
	}
}
