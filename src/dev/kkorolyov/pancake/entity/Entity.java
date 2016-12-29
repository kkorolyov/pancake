package dev.kkorolyov.pancake.entity;

/**
 * A single entity found in the game world.
 */
public class Entity {
	private static final int 	POSITION = 0,
														VELOCITY = 1;
	
	private int[][] location;
	private EntityController controller;
	
	/**
	 * Constructs a new entity with {@code 0} position and velocity along a number of axes.
	 * @param axes number of axes defining this entity's location
	 * @param controller entity controller
	 */
	public Entity(int axes, EntityController controller) {
		this(new int[axes][2], controller);
	}
	/**
	 * Constructs a new entity.
	 * @param location initial entity location determined by an arbitrary number of axes where
	 * 	<ul>
	 * 	<li>{@code location[axis][0] = position}</li>
	 * 	<li>{@code location[axis][1] = velocity}</li>
	 * 	</ul>
	 * @param controller entity controller
	 * @throws IllegalArgumentException if number of position and velocity axes differ
	 */
	public Entity(int[][] location, EntityController controller) {
		this.location = location;
		this.controller = controller;
	}
	
	/**
	 * Updates this entity by one tick.
	 */
	public void update() {
		stop();
		controller.update(this);
		updateLocation();
	}
	private void updateLocation() {
		for (int[] axis : location)
			axis[POSITION] += axis[VELOCITY];
	}
	
	/**
	 * Sets all velocities to {@code 0}.
	 */
	public void stop() {
		for (int[] axis : location)
			axis[1] = 0;
	}
	
	/** @return number of axes defining this entity's location */
	public int getAxes() {
		return location.length;
	}
	
	/**
	 * Returns the position along an axis.
	 * @param axis positive axis identifier (including {@code 0})
	 * @return position along axis, defaulting to {@code 0} if the axis is undefined for this entity
	 */
	public int getPosition(int axis) {
		return getLocation(axis, POSITION);
	}
	/**
	 * Sets the position along an axis, if it is defined.
	 * @param axis positive axis identifier (including {@code 0})
	 * @param position new position along axis
	 */
	public void setPosition(int axis, int position) {
		setLocation(axis, position, POSITION);
	}
	
	/**
	 * Returns the velocity along an axis.
	 * @param axis positive axis identifier (including {@code 0})
	 * @return position along axis, defaulting to {@code 0} if the axis is undefined for this entity
	 */
	public int getVelocity(int axis) {
		return getLocation(axis, VELOCITY);
	}
	/**
	 * Sets the velocity along an axis, if it is defined.
	 * @param axis positive axis identifier (including {@code 0})
	 * @param velocity new velocity along axis
	 */
	public void setVelocity(int axis, int velocity) {
		setLocation(axis, velocity, VELOCITY);
	}
	
	private int getLocation(int axis, int parameter) {
		return validAxis(axis) ? location[axis][parameter] : 0;
	}
	private void setLocation(int axis, int value, int parameter) {
		if (validAxis(axis))
			location[axis][parameter] = value;
	}
	private boolean validAxis(int axis) {
		return axis >= 0 && location.length > axis;
	}
}
