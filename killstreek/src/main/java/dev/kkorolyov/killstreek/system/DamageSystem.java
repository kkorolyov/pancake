package dev.kkorolyov.killstreek.system;

import dev.kkorolyov.killstreek.component.Damage;
import dev.kkorolyov.killstreek.component.Health;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.entity.Signature;
import dev.kkorolyov.pancake.platform.event.DestroyEntity;
import dev.kkorolyov.pancake.platform.utility.Limiter;

/**
 * Applies damage to entity health.
 * Removes dead entities.
 */
public class DamageSystem extends GameSystem {
	/**
	 * Constructs a new damage system.
	 */
	public DamageSystem() {
		super(
				new Signature(Health.class, Damage.class),
				new Limiter(0)
		);
	}

	@Override
	public void update(Entity entity, long dt) {
		Health health = entity.get(Health.class);
		Damage damage = entity.get(Damage.class);

		damage.apply(health, dt);

		if (health.isDead()) {
			resources.events.enqueue(new DestroyEntity(entity.getId()));
		}
	}
}
