package dev.kkorolyov.pancake.platform.action;

import dev.kkorolyov.pancake.platform.entity.Entity;

/**
 * An atomic alteration of an entity.
 */
@FunctionalInterface
public interface Action {
	/**
	 * Alters an entity in some way.
	 * @param entity entity to alter
	 */
	void apply(Entity entity);
}
