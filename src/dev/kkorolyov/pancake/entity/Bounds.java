package dev.kkorolyov.pancake.entity;

import java.util.Arrays;

/**
 * Defines boundaries with a position and size along an arbitrary number of axes.
 */
public class Bounds extends AxisParams {
	private static final int AXIS_PARAMS = 2;
	private static final int 	POSITION = 0,
														SIZE = 1;
	
	/**
	 * Constructs new boundaries with equals size along each axis.
	 * @param axes number of axes encompassed by these boundaries
	 * @param size size along each axis
	 */
	public Bounds(int axes, int size) {
		this(buildSizes(axes, size));
	}
	/**
	 * Constructs new boundaries initially positioned at point {@code 0} along each axis.
	 * @param sizes size of boundaries along each axis
	 */
	public Bounds(float... sizes) {
		super(sizes.length, AXIS_PARAMS);
		for (int i = 0; i < sizes.length; i++)
			values[i][SIZE] = sizes[i];
	}
	
	private static float[] buildSizes(int length, float size) {
		float[] sizes = new float[length];
		Arrays.fill(sizes, size);
		return sizes;
	}
	
	/**
	 * Moves these boundaries by some amount along axes.
	 * The number of axes translated is the minimum between {@code Bounds.axes()} and {@code deltas.length}.
	 * @param deltas translation magnitudes along axes
	 */
	public void translate(float... deltas) {
		int max = Math.min(axes(), deltas.length);
		for (int i = 0; i < max; i++)
			values[i][POSITION] += deltas[i];
	}
	
	/**
	 * Checks whether these boundaries intersect other boundaries.
	 * @param other boundaries against which to check intersection
	 * @return {@code true} if the boundaries intersect
	 */
	public boolean intesects(Bounds other) {
		int max = Math.min(axes(), other.axes());
		for (int i = 0; i < max; i++) {
			if ((values[i][POSITION] + values[i][SIZE]) < other.values[i][POSITION] || (other.values[i][POSITION] + other.values[i][SIZE]) < values[i][POSITION])
				return false;
		}
		return true;
	}
	
	/** @return array of positions of these boundaries along each encompassed axis */
	public float[] getPositions() {
		return getParams(POSITION);
	}
	/**
	 * @param axis axis number
	 * @return position along axis
	 * @throws IllegalArgumentException if {@code axis < 0} or {@code axis > Bounds.axes()}
	 */
	public float getPosition(int axis) {
		return getValue(axis, POSITION);
	}
	/**
	 * @param axis axis number
	 * @param position new position along axis
	 * @throws IllegalArgumentException if {@code axis < 0} or {@code axis > Bounds.axes()}
	 */
	public void setPosition(int axis, float position) {
		setValue(axis, POSITION, position);
	}
	
	/** @return array of sizes of these boundaries along each encompassed axis */
	public float[] getSizes() {
		return getParams(SIZE);
	}
	/**
	 * @param axis axis number
	 * @return size along axis
	 * @throws IllegalArgumentException if {@code axis < 0} or {@code axis > Bounds.axes()}
	 */
	public float getSize(int axis) {
		return getValue(axis, SIZE);
	}
	/**
	 * @param axis axis number
	 * @param size new size along axis, where anything {@code < 0} is treated as {@code 0}
	 * @throws IllegalArgumentException if {@code axis < 0} or {@code axis > Bounds.axes()}
	 */
	public void setSize(int axis, float size) {
		setValue(axis, SIZE, Math.max(0, size));
	}
}
