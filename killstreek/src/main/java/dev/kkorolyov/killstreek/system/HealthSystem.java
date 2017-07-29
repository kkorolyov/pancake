package dev.kkorolyov.killstreek.system;

import dev.kkorolyov.killstreek.component.Health;
import dev.kkorolyov.pancake.GameSystem;
import dev.kkorolyov.pancake.entity.Entity;
import dev.kkorolyov.pancake.entity.Signature;
import dev.kkorolyov.pancake.event.PlatformEvents;

/**
 * Cleans up not-so-healthy entities.
 */
public class HealthSystem extends GameSystem {
	/**
	 * Constructs a new health system.
	 */
	public HealthSystem() {
		super(new Signature(Health.class));
	}

	@Override
	public void update(Entity entity, float dt) {
		if (entity.get(Health.class).isDead()) {
			enqueue(PlatformEvents.DESTROY, entity, null);
		}
	}
}
