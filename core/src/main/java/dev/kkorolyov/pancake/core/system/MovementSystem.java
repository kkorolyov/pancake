package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.core.component.movement.Velocity;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;

/**
 * Applies velocity to entity positions.
 */
public class MovementSystem extends GameSystem {
	/**
	 * Constructs a new movement system.
	 */
	public MovementSystem() {
		super(Transform.class, Velocity.class);
	}

	@Override
	public void update(Entity entity, long dt) {
		Transform transform = entity.get(Transform.class);
		Velocity velocity = entity.get(Velocity.class);

		velocity.move(transform.getPosition(), dt / 1e9f);
	}
}
