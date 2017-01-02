package dev.kkorolyov.pancake.entity.collision;

/**
 * A vector defined as a head at some point in 3 dimensions and a tail at {@code (0, 0, 0)}.
 */
public class Vector {
	private float x, y, z;
	
	/**
	 * Clones a vector.
	 * @param original vector to clone
	 */
	public Vector(Vector original) {
		this(original.x, original.y, original.z);
	}
	/**
	 * Constructs a vector with a head at {@code (0, 0, 0)}.
	 */
	public Vector() {
		this(0, 0, 0);
	}
	/**
	 * Constructs a vector with a head at {@code (x, y, 0)}
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public Vector(float x, float y) {
		this(x, y, 0);
	}
	/**
	 * Constructs a vector with a head at {@code (x, y, z)}.
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param z z-coordinate
	 */
	public Vector(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Translates the head of this vector along 2 axes.
	 * @param dx x-coordinate change
	 * @param dy y-coordinate change
	 */
	public void translate(float dx, float dy) {
		translate(dx, dy, 0);
	}
	/**
	 * Translates the head of this vector along 3 axes.
	 * @param dx x-coordinate change
	 * @param dy y-coordinate change
	 * @param dz z-coordinate change
	 */
	public void translate(float dx, float dy, float dz) {
		x += dx;
		y += dy;
		z += dz;
	}
	
	/**
	 * Scales the magnitude of this vector by a specified value.
	 * @param value value to scale by
	 */
	public void scale(float value) {
		x *= value;
		y *= value;
		z *= value;
	}
	/**
	 * Scales this vector by another vector.
	 * @param other vector to scale by
	 */
	public void scale(Vector other) {
		x *= other.x;
		y *= other.x;
		z *= other.z;
	}
	
	/**
	 * Transforms this vector by adding another vector to it.
	 * This is the same as translating this vector by the other vector's components.
	 * @param other vector to add
	 */
	public void add(Vector other) {
		translate(other.x, other.y, other.z);
	}
	/**
	 * Transforms this vector by subtracting another vector from it.
	 * This is the same as translating this vector by the negative of the other vector's components.
	 * @param other vector to subtract
	 */
	public void sub(Vector other) {
		translate(-other.x, -other.y, -other.z);
	}
	
	/**
	 * Returns the dot product of this vector and another vector.
	 * @param other vector to dot-multiply with this vector
	 * @return result of dot product
	 */
	public float dot(Vector other) {
		return (x * other.x) + (y * other.y) + (z * other.z);
	}
	
	/**
	 * Returns the projection of another vector onto this vector. 
	 * @param other vector to project onto this vector
	 * @return result of projection
	 */
	public Vector project(Vector other) {
		float scale = dot(other) / dot(this);
		return new Vector(x * scale, y * scale, z * scale);
	}
	
	/**
	 * Normalizes this vector by scaling it into a unit vector pointing in the same direction.
	 */
	public void normalize() {
		scale(1 / getMagnitude());
	}	
	/**
	 * Calculates and returns the vector with {@code magnitude = 1} and same direction as this vector.
	 * @return unit vector with the same direction as this vector
	 */
	public Vector getUnitVector() {
		float scale = 1 / getMagnitude();
		return new Vector(x * scale, y * scale, z * scale);
	}
	
	/** @return magnitude of this vector */
	public float getMagnitude() {
		return (float) Math.sqrt(dot(this));
	}
	
	/**
	 * Sets the head of this vector, while retaining the current z-axis value.
	 * @param x new x-coordinate
	 * @param y new y-coordinate
	 */
	public void set(float x, float y) {
		set(x, y, z);
	}
	/**
	 * Sets the head of this vector.
	 * @param x new x-coordinate
	 * @param y new y-coordinate
	 * @param z new z-coordinate
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
	
	/** @return x-coordinate */
	public float getX() {
		return x;
	}
	/** @param x new x-coordinate */
	public void setX(float x) {
		this.x = x;
	}
	
	/** @return y-coordinate */
	public float getY() {
		return y;
	}
	/** @param y new y-coordinate */
	public void setY(float y) {
		this.y = y;
	}
	
	/** @return z-coordinate */
	public float getZ() {
		return z;
	}
	/** @param z new z-coordinate */
	public void setZ(float z) {
		this.z = z;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}
}
