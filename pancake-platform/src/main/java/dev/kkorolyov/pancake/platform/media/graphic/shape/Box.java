package dev.kkorolyov.pancake.platform.media.graphic.shape;

import dev.kkorolyov.pancake.platform.math.Vector;

/**
 * A 2 or 3 dimensional box.
 */
public abstract class Box extends Shape {
	private final Vector size = new Vector();

	/** @return mutable size vector */
	public final Vector getSize() {
		return size;
	}
}
