package dev.kkorolyov.pancake.component;

import dev.kkorolyov.pancake.Component;
import dev.kkorolyov.pancake.math.Vector;

/**
 * Defines abstract boundaries in relation to some origin point.
 */
public interface Bounds extends Component {
	/**
	 * Checks for intersection between bounds.
	 * @param other bounds against which to check intersection
	 * @return {@code true} if bounds intersect
	 */
	boolean intersects(Vector position, Bounds other);
}
