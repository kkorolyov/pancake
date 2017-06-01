package dev.kkorolyov.pancake.component.collision;

import dev.kkorolyov.pancake.Component;
import dev.kkorolyov.pancake.math.Vector;

/**
 * Defines abstract boundaries.
 */
public interface Bounds extends Component {
	/**
	 * Checks for intersection between bounds.
	 * @param other bounds against which to check intersection
	 * @param thisOrigin origin point of these bounds
	 * @param otherOrigin origin point of {@code other} bounds
	 * @return {@code true} if bounds intersect
	 */
	boolean intersects(Bounds other, Vector thisOrigin, Vector otherOrigin);
}
