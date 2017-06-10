package dev.kkorolyov.pancake.component.collision;

import dev.kkorolyov.pancake.entity.Component;
import dev.kkorolyov.pancake.math.Vector;

/**
 * Bounds defined by a rectangular prism.
 */
public class BoxBounds implements Component {
	private final Vector size;

	/**
	 * Constructs a new box.
	 * @param size box dimensions
	 */
	public BoxBounds(Vector size) {
		this.size = size;
	}

	/** @return size vector */
	public Vector getSize() {
		return size;
	}

	@Override
	public String toString() {
		return "BoxBounds{" +
					 "size=" + size +
					 '}';
	}
}
