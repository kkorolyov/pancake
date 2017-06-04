package dev.kkorolyov.pancake.component.collision;

import dev.kkorolyov.pancake.Component;

/**
 * Bounds defined by a sphere.
 */
public class SphereBounds implements Component {
	private final float radius;
	
	/**
	 * Constructs a new sphere.
	 * @param radius sphere radius
	 */
	public SphereBounds(float radius) {
		this.radius = radius;
	}

	/** @return sphere radius */
	public float getRadius() {
		return radius;
	}
}
