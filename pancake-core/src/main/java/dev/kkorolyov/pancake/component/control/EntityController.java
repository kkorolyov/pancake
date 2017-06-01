package dev.kkorolyov.pancake.component.control;

import dev.kkorolyov.pancake.core.Component;
import dev.kkorolyov.pancake.core.Entity;

/**
 * Updates {@code Entities} using discrete actions.
 * @see Entity
 */
public interface EntityController extends Component {
	/**
	 * Applies updates on an entity.
	 * @param entity entity to update
	 * @param dt elapsed time in seconds since last update
	 */
	void update(Entity entity, float dt);
}
