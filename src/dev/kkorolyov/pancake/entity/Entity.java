package dev.kkorolyov.pancake.entity;

/**
 * A single entity found in the game world.
 */
public class Entity {
	private int[] position,
								velocity;
	private EntityController controller;
	
	/**
	 * Constructs a new entity with {@code 0} position and velocity along a number of axes.
	 * @param axes number of axes defining this entity's location
	 * @param controller entity controller
	 */
	public Entity(int axes, EntityController controller) {
		this(new int[axes], new int[axes], controller);
	}
	/**
	 * Constructs a new entity.
	 * @param position initial entity position along an arbitrary number of axes
	 * @param velocity initial entity velocity along an arbitrary number of axes
	 * @param controller entity controller
	 * @throws IllegalArgumentException if number of position and velocity axes differ
	 */
	public Entity(int[] position, int[] velocity, EntityController controller) {
		if (position.length != velocity.length)
			throw new IllegalArgumentException("Number of position axes does not match number of velocity axes: " + position.length + " != " + velocity.length);
		
		this.position = position;
		this.velocity = velocity;
		
		this.controller = controller;
	}
	
	/**
	 * Updates this entity by one tick.
	 */
	public void update() {
		stop();
		controller.update(this);
		updatePosition();
	}
	private void updatePosition() {
		for (int i = 0; i < position.length; i++)
			position[i] += velocity[i];
	}
	
	/**
	 * Sets all velocities to {@code 0}.
	 */
	public void stop() {
		for (int i = 0; i < velocity.length; i++)
			velocity[i] = 0;
	}
	
	/** @return number of axes defining this entity's location */
	public int getAxes() {
		return position.length;
	}
	
	/**
	 * Returns the position along an axis.
	 * @param axis positive axis identifier (including {@code 0})
	 * @return position along axis, defaulting to {@code 0} if the axis is undefined for this entity
	 */
	public int getPosition(int axis) {
		return get(position, axis);
	}
	/**
	 * Sets the position along an axis, if it is defined.
	 * @param axis positive axis identifier (including {@code 0})
	 * @param position new position along axis
	 */
	public void setPosition(int axis, int position) {
		set(this.position, axis, position);
	}
	
	/**
	 * Returns the velocity along an axis.
	 * @param axis positive axis identifier (including {@code 0})
	 * @return position along axis, defaulting to {@code 0} if the axis is undefined for this entity
	 */
	public int getVelocity(int axis) {
		return get(velocity, axis);
	}
	/**
	 * Sets the velocity along an axis, if it is defined.
	 * @param axis positive axis identifier (including {@code 0})
	 * @param velocity new velocity along axis
	 */
	public void setVelocity(int axis, int velocity) {
		set(this.velocity, axis, velocity);
	}
	
	private static int get(int[] array, int index) {
		return (validateIndex(array, index) ? array[index] : 0);
	}
	private static void set(int[] array, int index, int value) {
		if (validateIndex(array, index))
			array[index] = value;
	}
	private static boolean validateIndex(int[] array, int index) {
		return index >= 0 && array.length > index;
	}
}
