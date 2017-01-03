package dev.kkorolyov.pancake.entity;

import dev.kkorolyov.pancake.entity.collision.Bounds;
import dev.kkorolyov.pancake.entity.control.EntityController;

/**
 * A single entity found in the game world.
 */
public class Entity {
	private final Bounds bounds;
	private final Body physics;
	private Sprite sprite;
	private EntityController controller;
	
	/**
	 * Constructs a new entity.
	 * @param bounds entity boundaries
	 * @param physics physics body representing this entity
	 * @param sprite graphical representation of this entity
	 * @param controller entity controller
	 */
	public Entity(Bounds bounds, Body physics, Sprite sprite, EntityController controller) {
		this.bounds = bounds;
		this.physics = physics;
		this.sprite = sprite;
		this.controller = controller;
	}
	
	/**
	 * Updates this entity.
	 * @param dt seconds elapsed between this and the last update
	 */
	public void update(float dt) {
		controller.update(this, dt);
		physics.update(dt);
		physics.apply(bounds);
	}
	
	/**
	 * @param other entity against which to check collision
	 * @return {@code true} if the entities collide
	 */
	public boolean collides(Entity other) {
		return bounds.intersects(other.bounds);
	}
	
	/** @return this entity's boundaries */
	public Bounds getBounds() {
		return bounds;
	}
	/** @return this entity's physics body */
	public Body getBody() {
		return physics;
	}
	/** @return this entity's graphical representation */
	public Sprite getSprite() {
		return sprite;
	}
	
	public void move(int axis, float acceleration) {
		// TODO
	}
	
	/** @param controller new entity controller */
	public void setController(EntityController controller) {
		this.controller = controller;
	}
}
