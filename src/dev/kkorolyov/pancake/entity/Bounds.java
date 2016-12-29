package dev.kkorolyov.pancake.entity;

import java.util.Arrays;

/**
 * Defines boundaries with a position and size along an arbitrary number of axes.
 */
public class Bounds {
	private static final int AXIS_PARAMS = 2;
	private static final int 	POSITION = 0,
														SIZE = 1;
	
	private final int[][] bounds;
	
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
	public Bounds(int... sizes) {
		bounds = new int[sizes.length][AXIS_PARAMS];
		for (int i = 0; i < sizes.length; i++)
			bounds[i][SIZE] = sizes[i];
	}
	
	private static int[] buildSizes(int length, int size) {
		int[] sizes = new int[length];
		Arrays.fill(sizes, size);
		return sizes;
	}
	
	/**
	 * Moves these boundaries by some amount along axes.
	 * The number of axes translated is the minimum between {@code Bounds.axes()} and {@code deltas.length}.
	 * @param deltas translation magnitudes along axes
	 */
	public void translate(int... deltas) {
		int max = Math.min(axes(), deltas.length);
		for (int i = 0; i < max; i++)
			bounds[i][POSITION] += deltas[i];
	}
	
	/**
	 * Checks whether these boundaries intersect other boundaries.
	 * @param other boundaries against which to check intersection
	 * @return {@code true} if the boundaries intersect
	 */
	public boolean intesects(Bounds other) {
		int max = Math.min(axes(), other.axes());
		for (int i = 0; i < max; i++) {
			if ((bounds[i][POSITION] + bounds[i][SIZE]) < other.bounds[i][POSITION] || (other.bounds[i][POSITION] + other.bounds[i][SIZE]) < bounds[i][POSITION])
				return false;
		}
		return true;
	}
	
	/** @return number of axes encompassed by these boundaries */
	public int axes() {
		return bounds.length;
	}
	
	/**
	 * @param axis axis number
	 * @return position along axis
	 * @throws IllegalArgumentException if {@code axis < 0} or {@code axis > Bounds.axes()}
	 */
	public int getPosition(int axis) {
		return getBounds(axis, POSITION);
	}
	/**
	 * @param axis axis number
	 * @param position new position along axis
	 * @throws IllegalArgumentException if {@code axis < 0} or {@code axis > Bounds.axes()}
	 */
	public void setPosition(int axis, int position) {
		setBounds(axis, position, POSITION);
	}
	
	/**
	 * @param axis axis number
	 * @return size along axis
	 * @throws IllegalArgumentException if {@code axis < 0} or {@code axis > Bounds.axes()}
	 */
	public int getSize(int axis) {
		return getBounds(axis, SIZE);
	}
	/**
	 * @param axis axis number
	 * @param size new size along axis, where anything {@code < 0} is treated as {@code 0}
	 * @throws IllegalArgumentException if {@code axis < 0} or {@code axis > Bounds.axes()}
	 */
	public void setSize(int axis, int size) {
		setBounds(axis, Math.max(size, 0), SIZE);
	}
	
	private int getBounds(int axis, int parameter) {
		validateAxis(axis);
		return bounds[axis][parameter];
	}
	private void setBounds(int axis, int value, int parameter) {
		validateAxis(axis);
		bounds[axis][parameter] = value;
	}
	private void validateAxis(int axis) {
		if (axis < 0)
			throw new IllegalArgumentException("Axis must be > 0: " + axis);
		if (axis >= axes())
			throw new IllegalArgumentException("Axis must be < number of axes: " + axis);
	}
}
