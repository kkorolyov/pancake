package dev.kkorolyov.pancake.system;

import dev.kkorolyov.pancake.component.*;
import dev.kkorolyov.pancake.component.movement.Damping;
import dev.kkorolyov.pancake.component.movement.Force;
import dev.kkorolyov.pancake.component.movement.MaxSpeed;
import dev.kkorolyov.pancake.component.movement.Velocity;
import dev.kkorolyov.pancake.core.Entity;
import dev.kkorolyov.pancake.core.GameSystem;
import dev.kkorolyov.pancake.core.Signature;

/**
 * Updates entities using physics calculations.
 */
public class MovementSystem extends GameSystem {
	/**
	 * Constructs a new physics system.
	 */
	public MovementSystem() {
		super(new Signature(Velocity.class,
												Force.class));
	}

	@Override
	public void update(Entity entity, float dt) {
		Velocity velocity = entity.get(Velocity.class);
		Force force = entity.get(Force.class);
		Damping damping = entity.get(Damping.class);
		MaxSpeed maxSpeed = entity.get(MaxSpeed.class);
		Transform transform = entity.get(Transform.class);

		if (damping != null) damping.damp(velocity.getVelocity(), force.getForce());

		force.accelerate(velocity.getVelocity(), dt);

		if (maxSpeed != null) maxSpeed.cap(velocity.getVelocity());

		velocity.round();

		if (transform != null) velocity.move(transform.getPosition(), dt);
	}
}
