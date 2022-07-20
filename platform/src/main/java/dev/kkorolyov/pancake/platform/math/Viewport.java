package dev.kkorolyov.pancake.platform.math;

/**
 * A constrained view on a renderable artifact.
 * Splits an overall artifact into a 3D set of partitions.
 * Partitioning constrained {@code [1, Integer#MAX_VALUE]} along each axis.
 */
public final class Viewport {
	private final Vector3 partitions;
	private final Vector3 current = Vector3.of(0, 0, 0);

	private final Vector3 origin = Vector3.of(0, 0, 0);
	private final Vector3 size = Vector3.of(0, 0, 0);

	private final Vector3 fullSize = Vector3.of(0, 0, 0);
	private final Vector3 lastFullSize = Vector3.of(0, 0, 0);

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
		partitions = Vector3.of(verify(xParts), verify(yParts), verify(zParts));
	}
	private static double verify(double value) {
		if (value < 1 || value > Integer.MAX_VALUE) throw new IllegalArgumentException("partition component must be >= 0 and <= MAX_INT; was " + value);
		return value;
	}

	/**
	 * Sets this viewport to {@code partition} index read {@code left->right, top->bottom, nearest->furthest}.
	 * @param partition index of partition to set to, constrained {@code [0, length()]}
	 */
	public void set(int partition) {
		partition = Math.max(0, Math.min(length(), partition));

		int parts2d = (int) (partitions.getX() * partitions.getY());
		int partXY = partition % parts2d;

		current.setX((int) (partXY % partitions.getX()));
		current.setY((int) (partXY / partitions.getX()));
		current.setZ(partition / parts2d);
	}

	/** @return number of partitions in this viewport */
	public int length() {
		return (int) (partitions.getX() * partitions.getY() * partitions.getZ());
	}

	/** @return {@link #getOrigin(double, double, double)} for a 2D viewport */
	public Vector2 getOrigin(double width, double height) {
		return getOrigin(width, height, 1);
	}
	/**
	 * @param width width of artifact to apply this viewport to
	 * @param height height of artifact to apply this viewport to
	 * @param depth depth of artifact to apply this viewport to
	 * @return viewport origin calculated according to given {@code width}, {@code height}, {@code depth}
	 */
	public Vector3 getOrigin(double width, double height, double depth) {
		calculate(width, height, depth);
		return origin;
	}

	/** @return {@link #getSize(double, double, double)} for a 2D viewport */
	public Vector2 getSize(double width, double height) {
		return getSize(width, height, 1);
	}
	/**
	 * @param width width of artifact to apply this viewport to
	 * @param height height of artifact to apply this viewport to
	 * @param depth depth of artifact to apply this viewport to
	 * @return viewport size calculated according to given {@code width}, {@code height}, {@code depth}
	 */
	public Vector3 getSize(double width, double height, double depth) {
		calculate(width, height, depth);
		return size;
	}

	private void calculate(double width, double height, double depth) {
		fullSize.setX(width);
		fullSize.setY(height);
		fullSize.setZ(depth);

		size.set(fullSize);
		size.setX(size.getX() / partitions.getX());
		size.setY(size.getY() / partitions.getY());
		size.setZ(size.getZ() / partitions.getZ());

		origin.set(current);
		origin.setX(origin.getX() * size.getX());
		origin.setY(origin.getY() * size.getY());
		origin.setZ(origin.getZ() * size.getZ());

		lastFullSize.set(fullSize);
	}
}
