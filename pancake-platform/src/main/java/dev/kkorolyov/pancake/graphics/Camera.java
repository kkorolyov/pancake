package dev.kkorolyov.pancake.graphics;

import dev.kkorolyov.pancake.math.Vector;

/**
 * Provides for mapping between absolute and relative positions.
 */
public class Camera {
	private Vector position;
	private Vector unitPixels = new Vector();
	private final Vector output = new Vector();
	private final Vector halfSize = new Vector();

	/**
	 * Constructs a new camera.
	 * @param position absolute camera position
	 * @param unitPixels number of pixels within 1 unit of absolute distance along each axis
	 * @param size dimensions of render medium
	 */
	public Camera(Vector position, Vector unitPixels, Vector size) {
		setPosition(position);
		setUnitPixels(unitPixels);
		setSize(size);
	}

	/**
	 * Calculates and returns the absolute position of a relative position vector.
	 * The returned vector is shared by all invocations of this method and {@link #getRelativePosition(Vector)}.
	 * @param relative relative position
	 * @return absolute position of {@code relative}
	 */
	public Vector getAbsolutePosition(Vector relative) {
		output.set(relative);
		output.sub(halfSize);
		output.invScale(unitPixels); // Scale to absolute coordinates
		output.add(position); // Position relative to origin

		return output;
	}
	/**
	 * Calculates and returns the relative position of an absolute position vector.
	 * The returned vector is shared by all invocations of this method and {@link #getAbsolutePosition(Vector)}.
	 * @param absolute absolute position
	 * @return position of {@code absolute} relative to this camera
	 */
	public Vector getRelativePosition(Vector absolute) {
		output.set(absolute);
		output.sub(position); // Position relative to camera
		output.scale(unitPixels);	// Scale to pixels
		output.add(halfSize);	// Position relative to display

		return output;
	}

	/** @return camera position */
	public Vector getPosition() {
		return position;
	}
	/** @param position new camera position vector */
	public void setPosition(Vector position) {
		this.position = position;
	}

	/** @return number of pixels within 1 unit of absolute distance along each axis */
	public Vector getUnitPixels() {
		return unitPixels;
	}
	/** @param unitPixels new number of pixels within 1 unit of absolute distance along each axis */
	public void setUnitPixels(Vector unitPixels) {
		this.unitPixels = unitPixels;
	}

	/** @param size new dimensions of render medium */
	public void setSize(Vector size) {
		halfSize.set(size);
		halfSize.scale(.5f);
	}
}
