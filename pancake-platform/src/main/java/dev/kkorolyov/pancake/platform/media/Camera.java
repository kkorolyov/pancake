package dev.kkorolyov.pancake.platform.media;

import dev.kkorolyov.pancake.platform.math.Vector;

/**
 * A viewport on a render medium providing for mapping between absolute and relative/rendered positions.
 */
public class Camera {
	private Vector position;
	private Vector unitPixels;
	private final Vector output = new Vector();
	private final Vector halfSize = new Vector();

	/**
	 * Constructs a new camera.
	 * @param position absolute camera position
	 * @param unitPixels number of pixels within 1 unit of absolute distance along each axis
	 * @param width render medium width
	 * @param height render medium height
	 */
	public Camera(Vector position, Vector unitPixels, double width, double height) {
		setPosition(position);
		setUnitPixels(unitPixels);
		setSize(width, height);
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

	/**
	 * @param width render medium width
	 * @param height render medium height
	 */
	public void setSize(double width, double height) {
		halfSize.set(width, height);
		halfSize.scale(.5f);
	}
}
