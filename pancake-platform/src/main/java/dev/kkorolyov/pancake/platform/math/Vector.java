package dev.kkorolyov.pancake.platform.math;

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
	 * Translates the head of this vector along 2 axes.
	 * @return {@code this}
	 * @see #translate(float, float, float)
	 */
	public Vector translate(float dx, float dy) {
		return translate(dx, dy, 0);
	}
	/**
	 * Translates the head of this vector along 3 axes.
	 * @param dx change along x-axis
	 * @param dy change along y-axis
	 * @param dz change along z-axis
	 * @return {@code this}
	 */
	public Vector translate(float dx, float dy, float dz) {
		x += dx;
		y += dy;
		z += dz;

		return this;
	}

	/**
	 * Scales this vector by a scalar.
	 * @param value value to scale by
	 * @return {@code this}
	 */
	public Vector scale(float value) {
		x *= value;
		y *= value;
		z *= value;

		return this;
	}
	/**
	 * Scales this vector by another vector, scaling each component individually by the other vector's respective component.
	 * @param other vector to scale by
	 * @return {@code this}
	 */
	public Vector scale(Vector other) {
		x *= other.x;
		y *= other.y;
		z *= other.z;

		return this;
	}
	/**
	 * Scales this vector by the inverse of another vector, dividing each component individually by the other vector's respective component.
	 * @param other vector to inverse scale by
	 * @return {@code this}
	 */
	public Vector invScale(Vector other) {
		x /= other.x;
		y /= other.y;
		z /= other.z;

		return this;
	}

	/**
	 * Transforms this vector by adding another vector to it.
	 * This is equivalent to translating this vector by the other vector's components.
	 * @param other vector to add
	 * @return {@code this}
	 * @see #add(Vector, float)
	 */
	public Vector add(Vector other) {
		return translate(other.x, other.y, other.z);
	}
	/**
	 * Transforms this vector by adding a scaled vector to it.
	 * @param other vector to add with scaling
	 * @param scale proportion of other vector's component values to add
	 * @return {@code this}
	 * @see #add(Vector)
	 */
	public Vector add(Vector other, float scale) {
		return translate(other.x * scale, other.y * scale, other.z * scale);
	}

	/**
	 * Transforms this vector by subtracting another vector from it.
	 * This is equivalent to translating this vector by the negative of the other vector's components.
	 * @param other vector to subtract
	 * @return {@code this}
	 * @see #sub(Vector, float)
	 */
	public Vector sub(Vector other) {
		return translate(-other.x, -other.y, -other.z);
	}
	/**
	 * Transforms this vector by subtracting a scaled vector from it.
	 * @param other vector to subtract with scaling
	 * @param scale proportion of other vector's component values to subtract
	 * @return {@code this}
	 * @see #sub(Vector)
	 */
	public Vector sub(Vector other, float scale) {
		return add(other, -scale);
	}

	/**
	 * Pivots this vector around its origin.
	 * @param theta radians to pivot x-y plane projection by, with respect to the positive x-axis
	 * @param phi radians to alter angle with the positive z-axis by
	 * @return {@code this}
	 */
	public Vector pivot(float theta, float phi) {
		float newX = (float) (x * Math.cos(theta) - y * Math.sin(theta));
		float newY = (float) (x * Math.sin(theta) + y * Math.cos(theta));
		float newZ = z;	// TODO

		set(newX, newY, newZ);

		return this;
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
	 * @return this
	 */
	public Vector normalize() {
		float magnitude = getMagnitude();
		return magnitude != 0 ? scale(1 / getMagnitude()) : this;
	}

	/**
	 * Calculates and returns the magnitude of this vector.
	 * @return magnitude of this vector
	 */
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
	 * Calculates and returns the angle between the positive x-axis and this vector's projection on the x-y plane.
	 * @return azimuthal angle in radians
	 */
	public float getTheta() {
		return (float) Math.atan2(y, x);
	}
	/**
	 * Calculates and returns the angle between the positive z-axis and this vector.
	 * @return polar angle in radians
	 */
	public float getPhi() {
		return (float) Math.acos(z / getMagnitude());
	}

	/**
	 * Sets the head of this vector while retaining the current z-axis value.
	 * @return {@code this}
	 * @see #set(float, float, float)
	 */
	public Vector set(float x, float y) {
		return set(x, y, z);
	}
	/**
	 * Sets the head of this vector.
	 * @param x new head x-coordinate
	 * @param y new head y-coordinate
	 * @param z new head z-coordinate
	 * @return {@code this}
	 */
	public Vector set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;

		return this;
	}
	/**
	 * Sets this vector equal to another vector.
	 * @param match vector to match
	 * @return {@code this}
	 */
	public Vector set(Vector match) {
		return set(match.x, match.y, match.z);
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
		return Float.compare(x, o.x) == 0
				&& Float.compare(y, o.y) == 0
				&& Float.compare(z, o.z) == 0;
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
