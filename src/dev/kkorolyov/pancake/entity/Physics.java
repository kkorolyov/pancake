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
														DAMPING = 2;
	private static final float 	DEFAULT_MAX_VELOCITY = 5,
															DEFAULT_DAMPING = .5f;
	
	private final Set<Integer> ignoreDamping = new HashSet<>();
	
	/**
	 * Constructs new physics at equilibrium along a set number of axes.
	 * @param axes number of axes encompassed by this object
	 */
	public Physics(int axes) {
		super(axes, AXIS_PARAMS);
		
		setParams(MAX_VELOCITY, DEFAULT_MAX_VELOCITY);
		setParams(DAMPING, DEFAULT_DAMPING);
	}
	
	public void update() {
		dampAll();
	}
	private void dampAll() {
		for (int i = 0; i < axes(); i++) {
			if (!ignoreDamping.remove(i))	// Removal and ignore check in one
				damp(i);
		}
	}
	private float damp(int axis) {
		return values[axis][VELOCITY] *= values[axis][DAMPING];
	}
	
	public void accelerate(int axis, float acceleration) {
		float velocity = getVelocity(axis),
					newVelocity = velocity + acceleration,
					maxVelocity = newVelocity < 0 ? -values[axis][MAX_VELOCITY]: values[axis][MAX_VELOCITY];
		float velocityMag = Math.abs(velocity),
					newVelocityMag = Math.abs(newVelocity),
					maxVelocityMag = Math.abs(maxVelocity);
		
		values[axis][VELOCITY] = newVelocityMag < maxVelocityMag ? (newVelocityMag < velocityMag ? damp(axis) + acceleration : newVelocity) : maxVelocity;	// If accel opposite velocity, damp velocity and reapply accel
		
		ignoreDamping.add(axis);
	}
	
	/** @return array of velocities along each encompassed axis */
	public float[] getVelocities() {
		return getParams(VELOCITY);
	}
	/**
	 * @param axis axis number
	 * @return velocity along {@code axis}
	 */
	public float getVelocity(int axis) {
		return getValue(axis, VELOCITY);
	}
	
	/** 
	 * @param axis axis number
	 * @param speed a positive number representing the maximum speed attainable along {@code axis}
	 */
	public void setMaxVelocity(int axis, float speed) {
		setValue(axis, MAX_VELOCITY, Math.max(0, speed));
	}
	/** 
	 * @param axis axis number 
	 * @param damping a number between 0 and 1 representing the amount by which velocity along {@code axis} is multiplied at each {@code update()} invocation; 0 equals immediate stop, 1 equals no damping 
	 */
	public void setDamping(int axis, float damping) {
		setValue(axis, DAMPING, Math.max(0, Math.min(1, damping)));
	}
}
