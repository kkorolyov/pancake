package dev.kkorolyov.pancake.entity;

import dev.kkorolyov.pancake.entity.control.EntityController;
import javafx.scene.image.Image;

/**
 * A single entity found in the game world.
 */
public class Entity {
	private final Bounds bounds;
	private final Physics physics;
	private Sprite sprite;
	private EntityController controller;
	
	/**
	 * Constructs a new entity.
	 * @param bounds boundaries of this entity
	 * @param physics physics acting on this entity
	 * @param sprite graphical representation of this entity
	 * @param controller entity controller
	 */
	public Entity(Bounds bounds, Physics physics, Sprite sprite, EntityController controller) {
		this.bounds = bounds;
		this.physics = physics;
		this.sprite = sprite;
		this.controller = controller;
	}
	
	/**
	 * Updates this entity by one tick.
	 */
	public void update() {
		controller.update(this);
		bounds.translate(physics.getVelocities());
		physics.update();
	}
	
	/**
	 * @param other entity against which to check collision
	 * @return {@code true} if the entities intersect
	 */
	public boolean collides(Entity other) {
		return bounds.intesects(other.bounds);
	}
	
	/** @return this entity's boundaries */
	public Bounds getBounds() {
		return bounds;
	}
	/** @return this entity's physics */
	public Physics getPhysics() {
		return physics;
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public void move(int axis, float acceleration) {
		physics.accelerate(axis, acceleration);
	}
	
	/** @param controller new entity controller */
	public void setController(EntityController controller) {
		this.controller = controller;
	}
}
