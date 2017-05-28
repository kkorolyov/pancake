package dev.kkorolyov.pancake.component.collision;

import dev.kkorolyov.pancake.core.Component;

/**
 * Defines abstract boundaries.
 */
public interface Bounds extends Component {
	/**
	 * Checks for intersection between bounds.
	 * @param other bounds against which to check intersection
	 * @return {@code true} if bounds intersect
	 */
	boolean intersects(Bounds other);
}
