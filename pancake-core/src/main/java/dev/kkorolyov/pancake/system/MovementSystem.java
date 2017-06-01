package dev.kkorolyov.pancake.system;

import dev.kkorolyov.pancake.component.Transform;
import dev.kkorolyov.pancake.component.movement.Velocity;
import dev.kkorolyov.pancake.Entity;
import dev.kkorolyov.pancake.GameSystem;
import dev.kkorolyov.pancake.Signature;

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
	public void update(Entity entity, float dt) {
		Transform transform = entity.get(Transform.class);
		Velocity velocity = entity.get(Velocity.class);

		velocity.move(transform.getPosition(), dt);
	}
}
