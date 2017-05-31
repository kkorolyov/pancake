package dev.kkorolyov.pancake.system;

import dev.kkorolyov.pancake.component.movement.MaxSpeed;
import dev.kkorolyov.pancake.component.movement.Velocity;
import dev.kkorolyov.pancake.core.Entity;
import dev.kkorolyov.pancake.core.GameSystem;
import dev.kkorolyov.pancake.core.Signature;

/**
 * Caps entity max speeds.
 */
public class SpeedCapSystem extends GameSystem {
	/**
	 * Constructs a new speed cap system.
	 */
	public SpeedCapSystem() {
		super(new Signature(MaxSpeed.class,
												Velocity.class));
	}

	@Override
	public void update(Entity entity, float dt) {
		MaxSpeed maxSpeed = entity.get(MaxSpeed.class);
		Velocity velocity = entity.get(Velocity.class);

		maxSpeed.cap(velocity.getVelocity());
	}
}
