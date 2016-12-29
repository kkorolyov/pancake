package dev.kkorolyov.pancake.entity;

import java.util.Arrays;

import dev.kkorolyov.pancake.entity.control.EntityController;

/**
 * A single entity found in the game world.
 */
public class Entity {
	private final Bounds bounds;
	private final int[] velocity;
	private EntityController controller;
	
	/**
	 * Constructs a new entity.
	 * @param bounds boundaries of this entity
	 * @param controller entity controller
	 */
	public Entity(Bounds bounds, EntityController controller) {
		this.bounds = bounds;
		this.controller = controller;
		
		velocity = new int[bounds.axes()];	// Velocity for each axis
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
		bounds.translate(velocity);
	}
	
	/**
	 * @param other entity against which to check collision
	 * @return {@code true} if the entities intersect
	 */
	public boolean collides(Entity other) {
		return bounds.intesects(other.bounds);
	}
	
	/**
	 * Sets velocity to {@code 0}.
	 */
	public void stop() {
		Arrays.fill(velocity, 0);
	}
	
	/** @return this entity's boundaries */
	public Bounds getBounds() {
		return bounds;
	}
	
	/**
	 * @param axis axis number
	 * @return velocity along axis
	 * @throws IllegalArgumentException if {@code axis < 0} or {@code axis >} number of velocity axes
	 */
	public int getVelocity(int axis) {
		validateAxis(axis);
		return velocity[axis];
	}
	/**
	 * @param axis axis number
	 * @param velocity new velocity along axis
	 * @throws IllegalArgumentException if {@code axis < 0} or {@code axis >} number of velocity axes
	 */
	public void setVelocity(int axis, int velocity) {
		validateAxis(axis);
		this.velocity[axis] = velocity;
	}
	
	private void validateAxis(int axis) {
		if (axis < 0)
			throw new IllegalArgumentException("Axis must be > 0: " + axis);
		if (axis >= velocity.length)
			throw new IllegalArgumentException("Axis must be < number of axes: " + axis);
	}
	
	/** @param controller new entity controller */
	public void setController(EntityController controller) {
		this.controller = controller;
	}
}
