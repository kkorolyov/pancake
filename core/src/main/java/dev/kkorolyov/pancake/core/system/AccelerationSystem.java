package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.Force;
import dev.kkorolyov.pancake.core.component.Mass;
import dev.kkorolyov.pancake.core.component.Velocity;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;

/**
 * Accelerates entities by force.
 */
public final class AccelerationSystem extends GameSystem {
	/**
	 * Constructs a new acceleration system.
	 */
	public AccelerationSystem() {
		super(Velocity.class, Force.class, Mass.class);
	}

	@Override
	protected void update(Entity entity, long dt) {
		Velocity velocity = entity.get(Velocity.class);
		Force force = entity.get(Force.class);
		Mass mass = entity.get(Mass.class);

		force.accelerate(velocity.getLinear(), mass.getValue(), dt / 1e9);
	}
}
