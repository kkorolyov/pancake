package dev.kkorolyov.pancake.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * Defines velocity and acceleration along a number of axes and provides for physics-based updating of these values.
 */
public class Physics extends AxisParams {
	private static final int AXIS_PARAMS = 3;
	private static final int 	VELOCITY = 0,
														MAX_VELOCITY = 1,
														FRICTION = 2;
	private static final float 	DEFAULT_MAX_VELOCITY = 5,
															DEFAULT_FRICTION = .5f;
	
	private final Set<Integer> frictionlessAxes = new HashSet<>();
	
	/**
	 * Constructs new physics at equilibrium along a set number of axes.
	 * @param axes number of axes encompassed by this object
	 */
	public Physics(int axes) {
		super(axes, AXIS_PARAMS);
		
		setParams(MAX_VELOCITY, DEFAULT_MAX_VELOCITY);
		setParams(FRICTION, DEFAULT_FRICTION);
	}
	
	public void update() {
		applyFriction();
	}
	private void applyFriction() {
		for (int i = 0; i < axes(); i++) {
			if (!frictionlessAxes.remove(i))	// Removal and ignore check in one
				values[i][VELOCITY] *= values[i][FRICTION];
		}
	}
	
	public void accelerate(int axis, float acceleration) {
		validateAxis(axis);
		
		float newVelocity = values[axis][VELOCITY] + acceleration,
					maxVelocity = newVelocity < 0 ? values[axis][MAX_VELOCITY] * -1: values[axis][MAX_VELOCITY];
		values[axis][VELOCITY] = Math.abs(newVelocity) <= Math.abs(maxVelocity) ? newVelocity : maxVelocity;
		
		frictionlessAxes.add(axis);
	}
	
	/** @return array of velocities along each encompassed axis */
	public float[] getVelocities() {
		return getParams(VELOCITY);
	}
	
	/** 
	 * @param axis axis number
	 * @param speed a positive integer representing the maximum speed attainable along {@code axis}
	 */
	public void setMaxVelocity(int axis, int speed) {
		setValue(axis, MAX_VELOCITY, Math.max(0, speed));
	}
	/** 
	 * @param axis axis number 
	 * @param rate a positive integer representing the amount by which acceleration along {@code axis} is decremented at the end of each {@code update()} invocation
	 */
	public void setFriction(int axis, int rate) {
		setValue(axis, FRICTION, Math.max(0, rate));
	}
}
