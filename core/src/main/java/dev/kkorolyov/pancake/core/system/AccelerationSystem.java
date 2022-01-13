package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.movement.Force;
import dev.kkorolyov.pancake.core.component.movement.Mass;
import dev.kkorolyov.pancake.core.component.movement.Velocity;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.utility.Limiter;

import java.util.List;

/**
 * Accelerates entities by force.
 */
public class AccelerationSystem extends GameSystem {
	/**
	 * Constructs a new acceleration system.
	 */
	public AccelerationSystem() {
		super(
				List.of(Velocity.class, Force.class, Mass.class),
				Limiter.fromConfig(AccelerationSystem.class)
		);
	}

	@Override
	public void update(Entity entity, long dt) {
		Velocity velocity = entity.get(Velocity.class);
		Force force = entity.get(Force.class);
		Mass mass = entity.get(Mass.class);

		force.accelerate(velocity.getValue(), mass.getValue(), dt / 1e9);
	}
}
