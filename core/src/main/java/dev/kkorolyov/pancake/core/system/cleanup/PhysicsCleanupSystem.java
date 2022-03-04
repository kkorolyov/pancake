package dev.kkorolyov.pancake.core.system.cleanup;

import dev.kkorolyov.pancake.core.component.event.Intersecting;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;

/**
 * Removes physics-related event components.
 */
public final class PhysicsCleanupSystem extends GameSystem {
	/**
	 * Constructs a new physics cleanup system.
	 */
	public PhysicsCleanupSystem() {
		super(Intersecting.class);
	}

	@Override
	protected void update(Entity entity, long dt) {
		entity.remove(Intersecting.class);
	}
}
