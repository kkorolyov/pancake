package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.movement.Force;
import dev.kkorolyov.pancake.core.component.movement.Velocity;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.entity.Signature;
import dev.kkorolyov.pancake.platform.utility.Limiter;

/**
 * Accelerates entities by force.
 * Caps velocity to maximum speed after accelerating.
 */
public class AccelerationSystem extends GameSystem {
	/**
	 * Constructs a new acceleration system.
	 */
	public AccelerationSystem() {
		super(
				new Signature(Velocity.class, Force.class),
				Limiter.fromConfig(AccelerationSystem.class)
		);
	}

	@Override
	public void update(Entity entity, long dt) {
		Velocity velocity = entity.get(Velocity.class);
		Force force = entity.get(Force.class);

		force.accelerate(velocity.getVelocity(), dt / 1e9f);
		velocity.cap();
	}
}
