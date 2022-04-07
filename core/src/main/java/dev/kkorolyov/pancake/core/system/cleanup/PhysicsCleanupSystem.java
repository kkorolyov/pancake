package dev.kkorolyov.pancake.core.system.cleanup;

import dev.kkorolyov.pancake.core.component.event.Intersected;
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
		super(Intersected.class);
	}

	@Override
	protected void update(Entity entity, long dt) {
		entity.remove(Intersected.class);
	}
}
