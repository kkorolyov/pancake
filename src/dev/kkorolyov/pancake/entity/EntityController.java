package dev.kkorolyov.pancake.entity;

/**
 * Updates {@code Entities} using discrete {@code Actions}.
 * @see Entity
 * @see Action
 */
public interface EntityController {
	/**
	 * Applies updates on an entity.
	 * @param entity entity to update
	 */
	void update(Entity entity);
}
