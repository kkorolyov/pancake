package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.movement.Velocity;
import dev.kkorolyov.pancake.core.component.movement.VelocityCap;
import dev.kkorolyov.pancake.platform.plugin.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.entity.Signature;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.utility.Limiter;

/**
 * Caps velocities of entities.
 */
public final class CappingSystem extends GameSystem {
	/**
	 * Constructs a new capping system.
	 */
	public CappingSystem() {
		super(
				new Signature(Velocity.class, VelocityCap.class),
				Limiter.fromConfig(CappingSystem.class)
		);
	}
	@Override
	public void update(Entity entity, long dt) {
		VelocityCap velocityCap = entity.get(VelocityCap.class);
		Vector3 velocity = entity.get(Velocity.class).getValue();

		velocityCap.cap(velocity);
	}
}
