package dev.kkorolyov.pancake.entity;

/**
 * A single entity found in the game world.
 */
public class Entity {
	private DynamicPoint position;
	private EntityController controller;

	/**
	 * Constructs a new entity.
	 * @param position initial entity position
	 * @param controller entity controller
	 */
	public Entity(DynamicPoint position, EntityController controller) {
		this.position = position;
		this.controller = controller;
	}
	
	/** @return dynamic point used for maintaining this entity's position */
	public DynamicPoint getPosition() {
		return position;
	}
	
	/**
	 * Updates this entity.
	 */
	public void update() {
		position.stop();
		controller.update(this);
		position.update();
	}
}
