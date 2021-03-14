package dev.kkorolyov.pancake.platform.math;

/**
 * A vector with min and max constraints along each axis.
 */
public final class BoundedVector extends Vector {
	private final Vector min, max;

	/**
	 * Constructs a new bounded vector.
	 * @param vector vector head
	 * @param min minimum constraints along each axis
	 * @param max maximum constraints along each axis
	 */
	public BoundedVector(Vector vector, Vector min, Vector max) {
		this.min = min;
		this.max = max;
		set(vector);
	}

	@Override
	public void setX(double x) {
		super.setX(Math.max(min.getX(), Math.min(max.getX(), x)));
	}
	@Override
	public void setY(double y) {
		super.setY(Math.max(min.getY(), Math.min(max.getY(), y)));
	}
	@Override
	public void setZ(double z) {
		super.setZ(Math.max(min.getZ(), Math.min(max.getZ(), z)));
	}
}
