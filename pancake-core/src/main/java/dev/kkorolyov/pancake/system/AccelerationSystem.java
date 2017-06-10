package dev.kkorolyov.pancake.system;

import dev.kkorolyov.pancake.component.movement.Force;
import dev.kkorolyov.pancake.component.movement.Velocity;
import dev.kkorolyov.pancake.entity.Entity;
import dev.kkorolyov.pancake.GameSystem;
import dev.kkorolyov.pancake.entity.Signature;

/**
 * Accelerates entities by force.
 */
public class AccelerationSystem extends GameSystem {
	/**
	 * Constructs a new acceleration system.
	 */
	public AccelerationSystem() {
		super(new Signature(Velocity.class,
												Force.class));
	}

	@Override
	public void update(Entity entity, float dt) {
		Velocity velocity = entity.get(Velocity.class);
		Force force = entity.get(Force.class);

		force.accelerate(velocity.getVelocity(), dt);
		velocity.round();
	}
}
