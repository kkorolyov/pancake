package dev.kkorolyov.pancake.control;

import dev.kkorolyov.pancake.engine.Entity;

/**
 * An action performed by an entity.
 */
@FunctionalInterface
public interface Action {
	/**
	 * Executes this action on an entity.
	 * @param entity entity which performs this action
	 */
	void execute(Entity entity);
}
