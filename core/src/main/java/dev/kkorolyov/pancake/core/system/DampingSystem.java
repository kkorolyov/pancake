package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.movement.Damping;
import dev.kkorolyov.pancake.core.component.movement.Force;
import dev.kkorolyov.pancake.core.component.movement.Velocity;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;

/**
 * Applies damping to entity velocities.
 */
public class DampingSystem extends GameSystem {
	/**
	 * Constructs a new damping system.
	 */
	public DampingSystem() {
		super(Damping.class, Velocity.class, Force.class);
	}

	@Override
	protected void update(Entity entity, long dt) {
		Damping damping = entity.get(Damping.class);
		Velocity velocity = entity.get(Velocity.class);
		Force force = entity.get(Force.class);

		damping.damp(velocity.getValue(), force.getValue());
	}
}
