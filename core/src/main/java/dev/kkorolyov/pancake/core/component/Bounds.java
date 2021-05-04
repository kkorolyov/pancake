package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.math.Vector3;

/**
 * Bounds defined by at least one of:
 * <pre>
 *   - Box
 *   - Sphere
 * </pre>
 */
public class Bounds implements Component {
	/** Intersection type between 2 boxes */
	public static final int BOX_BOX = 0;
	/** Intersection type between 2 spheres */
	public static final int SPHERE_SPHERE = 1;
	/** Intersection type between a box and sphere */
	public static final int BOX_SPHERE = 2;
	/** Intersection type between a sphere and a box */
	public static final int SPHERE_BOX = 3;

	private Vector3 box;
	private double radius;

	/**
	 * Constructs new bounds defined solely by a box.
	 * @see #Bounds(Vector3, Double)
	 */
	public Bounds(Vector3 box) {
		this(box, null);
	}
	/**
	 * Constructs new bounds defined solely by a sphere.
	 * @see #Bounds(Vector3, Double)
	 */
	public Bounds(Double radius) {
		this(null, radius);
	}
	/**
	 * Constructs new bounds.
	 * @param box box dimensions
	 * @param radius sphere radius
	 * @throws IllegalStateException if both {@code box} and {@code radius} are {@code null}
	 */
	public Bounds(Vector3 box, Double radius) {
		this.box = box;
		this.radius = (radius != null) ? radius : 0;

		verifyDefined();
	}

	/**
	 * Returns the type of intersection occurring between this bounds and {@code other}.
	 * e.g. Box-Box, Sphere-Sphere, Box-Sphere.
	 * @param other intersected bounds
	 * @return preferred intersection type between {@code this} and {@code other}
	 */
	public int getIntersectionType(Bounds other) {
		return hasRadius()
				? other.hasRadius() ? SPHERE_SPHERE
					: hasBox() ? BOX_BOX : SPHERE_BOX
				: other.hasBox() ? BOX_BOX : BOX_SPHERE;
	}

	/** @return {@code true} if this bounds is defined by a box */
	public boolean hasBox() {
		return box != null;
	}
	/** @return {@code true} if this bounds is defined by a sphere */
	public boolean hasRadius() {
		return radius > 0;
	}

	/** @return box dimensions, or {@code null} if not set */
	public Vector3 getBox() {
		return box;
	}
	/**
	 * @param box new box dimensions, {@code null} clears current value
	 * @return {@code this}
	 * @throws IllegalStateException if both {@code box} and {@code radius} of this instance are {@code null}
	 */
	public Bounds setBox(Vector3 box) {
		this.box = box;
		verifyDefined();

		return this;
	}

	/** @return sphere radius, or a value {@code <= 0} if not set */
	public double getRadius() {
		return radius;
	}
	/**
	 * @param radius new sphere radius, {@code null} clears current value
	 * @return {@code this}
	 * @throws IllegalStateException if both {@code box} and {@code radius} of this instance are {@code null}
	 */
	public Bounds setRadius(Double radius) {
		this.radius = (radius != null) ? radius : 0;
		verifyDefined();

		return this;
	}

	private void verifyDefined() {
		if (box == null && radius <= 0) {
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
