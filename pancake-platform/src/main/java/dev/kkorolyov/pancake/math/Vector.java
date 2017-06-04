package dev.kkorolyov.pancake.math;

import java.util.Objects;

/**
 * A vector defined as a head at some point in 3 dimensions and a tail at {@code (0, 0, 0)}.
 */
public class Vector {
	private float x, y, z;

	/**
	 * Constructs a vector with a head at {@code (0, 0, 0)}.
	 */
	public Vector() {
		this(0, 0, 0);
	}
	/**
	 * Constructs a vector with a head at {@code (x, y, 0)}.
	 * @see #Vector(float, float, float)
	 */
	public Vector(float x, float y) {
		this(x, y, 0);
	}
	/**
	 * Constructs a vector with a head at {@code (x, y, z)}.
	 * @param x head x-coordinate
	 * @param y head y-coordinate
	 * @param z head z-coordinate
	 */
	public Vector(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	/**
	 * Constructs a copy of a vector.
	 * @param original vector to copy
	 */
	public Vector(Vector original) {
		this(original.x, original.y, original.z);
	}

	/**
	 * Translates the head of this vector along 2 axes.
	 * @see #translate(float, float, float)
	 */
	public void translate(float dx, float dy) {
		translate(dx, dy, 0);
	}
	/**
	 * Translates the head of this vector along 3 axes.
	 * @param dx change along x-axis
	 * @param dy change along y-axis
	 * @param dz change along z-axis
	 */
	public void translate(float dx, float dy, float dz) {
		x += dx;
		y += dy;
		z += dz;
	}

	/**
	 * Scales this vector by a scalar.
	 * @param value value to scale by
	 */
	public void scale(float value) {
		x *= value;
		y *= value;
		z *= value;
	}
	/**
	 * Scales this vector by another vector, scaling each component individually by the other vector's respective component.
	 * @param other vector to scale by
	 */
	public void scale(Vector other) {
		x *= other.x;
		y *= other.y;
		z *= other.z;
	}

	/**
	 * Returns a new vector formed by the addition of vectors to an initial vector.
	 * @param vI initial vector
	 * @param vN added vectors
	 * @return vector formed by {@code v1 + vN}
	 */
	public static Vector add(Vector vI, Vector... vN) {
		Vector result = new Vector(vI);

		for (Vector v : vN) result.add(v);

		return result;
	}
	/**
	 * Returns a new vector formed by the subtraction of vectors from an initial vector.
	 * @param vI initial vector
	 * @param vN subtracted vectors
	 * @return vector formed by {@code v1 - vN}
	 */
	public static Vector sub(Vector vI, Vector... vN) {
		Vector result = new Vector(vI);

		for (Vector v : vN) result.sub(v);

		return result;
	}

	/**
	 * Transforms this vector by adding another vector to it.
	 * This is equivalent to translating this vector by the other vector's components.
	 * @param other vector to add
	 * @see #add(Vector, float)
	 */
	public void add(Vector other) {
		translate(other.x, other.y, other.z);
	}
	/**
	 * Transforms this vector by adding a scaled vector to it.
	 * @param other vector to add with scaling
	 * @param scale proportion of other vector's component values to add
	 * @see #add(Vector)
	 */
	public void add(Vector other, float scale) {
		translate(other.x * scale, other.y * scale, other.z * scale);
	}

	/**
	 * Transforms this vector by subtracting another vector from it.
	 * This is equivalent to translating this vector by the negative of the other vector's components.
	 * @param other vector to subtract
	 * @see #sub(Vector, float)
	 */
	public void sub(Vector other) {
		translate(-other.x, -other.y, -other.z);
	}
	/**
	 * Transforms this vector by subtracting a scaled vector from it.
	 * @param other vector to subtract with scaling
	 * @param scale proportion of other vector's component values to subtract
	 * @see #sub(Vector)
	 */
	public void sub(Vector other, float scale) {
		add(other, -scale);
	}

	/**
	 * Returns the dot product of this vector and another vector.
	 * @param other vector to dot-multiply with this vector
	 * @return dot product of vectors
	 */
	public float dot(Vector other) {
		return (x * other.x) + (y * other.y) + (z * other.z);
	}

	/**
	 * Returns the projection of another vector onto this vector.
	 * @param other vector to project onto this vector
	 * @return projected vector
	 */
	public Vector project(Vector other) {
		float scale = dot(other) / dot(this);
		return new Vector(x * scale, y * scale, z * scale);
	}

	/** @return Euclidean distance between this vector and {@code other} */
	public float distance(Vector other) {
		return (float) Math.sqrt(Math.pow(x - other.x, 2) +
														 Math.pow(y - other.y, 2) +
														 Math.pow(z - other.z, 2));
	}

	/**
	 * Returns the angle between this vector and another vector.
	 * @param other vector forming other end of angle
	 * @return angle between vectors
	 */
	public float angle(Vector other) {
		return (float) Math.acos(dot(other) / (getMagnitude() * other.getMagnitude()));
	}

	/**
	 * Scales this vector to a unit vector pointing in the original direction.
	 */
	public void normalize() {
		scale(1 / getMagnitude());
	}

	/** @return magnitude of this vector */
	public float getMagnitude() {
		return (float) Math.sqrt(dot(this));
	}
	/**
	 * Calculates and returns the vector with {@code magnitude = 1} and same direction as this vector.
	 * @return unit vector with the same direction as this vector
	 */
	public Vector getDirection() {
		float scale = 1 / getMagnitude();
		return new Vector(x * scale, y * scale, z * scale);
	}

	/**
	 * Sets the head of this vector while retaining the current z-axis value.
	 * @see #set(float, float, float)
	 */
	public void set(float x, float y) {
		set(x, y, z);
	}
	/**
	 * Sets the head of this vector.
	 * @param x new head x-coordinate
	 * @param y new head y-coordinate
	 * @param z new head z-coordinate
	 */
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	/**
	 * Sets this vector equal to another vector.
	 * @param match vector to match
	 */
	public void set(Vector match) {
		set(match.x, match.y, match.z);
	}

	/** @return head x-coordinate */
	public float getX() {
		return x;
	}
	/** @param x new head x-coordinate */
	public void setX(float x) {
		this.x = x;
	}

	/** @return head y-coordinate */
	public float getY() {
		return y;
	}
	/** @param y new head y-coordinate */
	public void setY(float y) {
		this.y = y;
	}

	/** @return head z-coordinate */
	public float getZ() {
		return z;
	}
	/** @param z new head z-coordinate */
	public void setZ(float z) {
		this.z = z;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || !(obj instanceof Vector)) return false;

		Vector o = (Vector) obj;

		return x == o.x
				&& y == o.y
				&& z == o.z;
	}
	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}

	/** @return coordinates of this vector's head as {@code (x, y, z)} */
	@Override
	public String toString() {
		return String.format("(%.3f, %.3f, %.3f)", x, y, z);
	}
}
