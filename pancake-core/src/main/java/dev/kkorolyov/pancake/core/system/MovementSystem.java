package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.core.component.movement.Velocity;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Signature;

/**
 * Applies velocity to entity positions.
 */
public class MovementSystem extends GameSystem {
	/**
	 * Constructs a new movement system.
	 */
	public MovementSystem() {
		super(new Signature(Transform.class,
												Velocity.class));
	}

	@Override
	public void update(int id, float dt) {
		Transform transform = entities.get(id, Transform.class);
		Velocity velocity = entities.get(id, Velocity.class);

		velocity.move(transform.getPosition(), dt);
	}
}
