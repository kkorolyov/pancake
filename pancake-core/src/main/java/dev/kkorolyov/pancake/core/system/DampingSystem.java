package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.movement.Damping;
import dev.kkorolyov.pancake.core.component.movement.Force;
import dev.kkorolyov.pancake.core.component.movement.Velocity;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Signature;

/**
 * Applies damping to entity velocities.
 */
public class DampingSystem extends GameSystem {
	/**
	 * Constructs a new damping system.
	 */
	public DampingSystem() {
		super(new Signature(Damping.class,
												Velocity.class,
												Force.class));
	}

	@Override
	public void update(int id, float dt) {
		Damping damping = entities.get(id, Damping.class);
		Velocity velocity = entities.get(id, Velocity.class);
		Force force = entities.get(id, Force.class);

		damping.damp(velocity.getVelocity(), force.getForce());
	}
}
