package dev.kkorolyov.pancake.platform.media.graphic.shape;

import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.math.Vectors;

/**
 * A 2 or 3 dimensional box.
 */
public abstract class Box extends Shape {
	private final Vector3 size = Vectors.create(0, 0, 0);

	/** @return mutable size vector */
	public final Vector3 getSize() {
		return size;
	}
}
