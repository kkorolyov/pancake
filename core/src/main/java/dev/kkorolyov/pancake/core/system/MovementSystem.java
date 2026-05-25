package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.core.component.Velocity;
import dev.kkorolyov.pancake.core.component.event.Moved;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;

/**
 * Applies velocity to entity positions.
 */
public final class MovementSystem extends GameSystem {
	private static final Velocity stationary = new Velocity();

	/**
	 * Constructs a new movement system.
	 */
	public MovementSystem() {
		super(Transform.class, Velocity.class);
	}

	@Override
	protected void update(Entity entity, long dt) {
		Transform transform = entity.get(Transform.class);
		Velocity velocity = entity.get(Velocity.class);

		if (!velocity.equals(stationary)) {
			double interval = dt / 1e9f;

			velocity.move(transform.getTranslation(), interval);
			velocity.rotate(transform.getRotation(), interval);

			entity.put(new Moved());
		}
	}
}
