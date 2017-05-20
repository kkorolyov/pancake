package dev.kkorolyov.pancake.entity.control;

import dev.kkorolyov.pancake.entity.Entity;

/**
 * Updates {@code Entities} using discrete {@code Actions}.
 * @see Entity
 * @see Action
 */
public interface EntityController {
	/**
	 * Applies updates on an entity.
	 * @param entity entity to update
	 * @param dt elapsed time in seconds since last update
	 */
	void update(Entity entity, float dt);
}
