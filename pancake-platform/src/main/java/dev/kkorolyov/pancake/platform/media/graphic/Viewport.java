package dev.kkorolyov.pancake.platform.media.graphic;

import dev.kkorolyov.pancake.platform.math.Vector;

/**
 * A constrained view on a renderable artifact.
 * Splits an overall artifact into a 3D set of partitions.
 * Partitioning constrained {@code [1, Integer#MAX_VALUE]} along each axis.
 */
public final class Viewport {
	private final Vector partitions;
	private final Vector current = new Vector();

	private final Vector origin = new Vector();
	private final Vector size = new Vector();

	private final Vector fullSize = new Vector();
	private final Vector lastFullSize = new Vector();

	/**
	 * Constructs a new 2D viewport.
	 * @see #Viewport(int, int, int)
	 */
	public Viewport(int xParts, int yParts) {
		this(xParts, yParts, 1);
	}
	/**
	 * Constructs a new viewport.
	 * @param xParts number of partitions along x-axis
	 * @param yParts number of partitions along y-axis
	 * @param zParts number of partitions along z-axis
	 */
	public Viewport(int xParts, int yParts, int zParts) {
		partitions = new Vector(constrain(xParts), constrain(yParts), constrain(zParts));
	}
	private static double constrain(double value) {
		return Math.max(1, Math.min(Integer.MAX_VALUE, value));
	}

	/**
	 * Sets this viewport to {@code partition} index read {@code left->right, top->bottom, nearest->furthest}.
	 * @param partition index of partition to set to, constrained {@code [0, length()]}
	 * @return {@code this}
	 */
	public Viewport set(int partition) {
		partition = Math.max(0, Math.min(length(), partition));

		int parts2d = (int) (partitions.getX() * partitions.getY());
		int partXY = partition % parts2d;

		current.set(
				(int) (partXY % partitions.getX()),
				(int) (partXY / partitions.getX()),
				partition / parts2d
		);
		return this;
	}

	/** @return number of partitions in this viewport */
	public int length() {
		return (int) (partitions.getX() * partitions.getY() * partitions.getZ());
	}

	/** @return {@link #getOrigin(double, double, double)} for a 2D viewport */
	public Vector getOrigin(double width, double height) {
		return getOrigin(width, height, 1);
	}
	/**
	 * @param width width of artifact to apply this viewport to
	 * @param height height of artifact to apply this viewport to
	 * @param depth depth of artifact to apply this viewport to
	 * @return viewport origin calculated according to given {@code width}, {@code height}, {@code depth}
	 */
	public Vector getOrigin(double width, double height, double depth) {
		calculate(width, height, depth);
		return origin;
	}

	/** @return {@link #getSize(double, double, double)} for a 2D viewport */
	public Vector getSize(double width, double height) {
		return getSize(width, height, 1);
	}
	/**
	 * @param width width of artifact to apply this viewport to
	 * @param height height of artifact to apply this viewport to
	 * @param depth depth of artifact to apply this viewport to
	 * @return viewport size calculated according to given {@code width}, {@code height}, {@code depth}
	 */
	public Vector getSize(double width, double height, double depth) {
		calculate(width, height, depth);
		return size;
	}

	private void calculate(double width, double height, double depth) {
		fullSize.set(width, height, depth);

		size.set(fullSize);
		size.invScale(partitions);

		origin.set(current);
		origin.scale(size);

		lastFullSize.set(fullSize);
	}
}
