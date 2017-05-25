package dev.kkorolyov.pancake.entity.collision;

import dev.kkorolyov.pancake.component.Bounds;
import dev.kkorolyov.pancake.math.Vector;

/**
 * Bounds defined by an elliptical area.
 */
public class EllipseBounds implements Bounds {
	private final Vector center;
	private float a, b;
	
	/**
	 * Constructs a new ellipse 
	 * @param center ellipse center
	 * @param a radius along x-axis
	 * @param b radius along y-axis
	 */
	public EllipseBounds(Vector center, float a, float b) {
		this.center = center;
		this.a = a;
		this.b = b;
	}
	
	@Override
	public boolean intersects(Bounds other) {
		/* TODO Auto-generated method stub */
		return false;
	}

	@Override
	public Vector getOrigin() {
		return center;
	}
}
