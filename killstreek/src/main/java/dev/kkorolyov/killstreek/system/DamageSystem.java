package dev.kkorolyov.killstreek.system;

import dev.kkorolyov.killstreek.component.Damage;
import dev.kkorolyov.killstreek.component.Health;
import dev.kkorolyov.killstreek.event.ResetDamage;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Signature;
import dev.kkorolyov.pancake.platform.event.DestroyEntity;

/**
 * Applies damage to entity health.
 * Removes dead entities.
 */
public class DamageSystem extends GameSystem {
	/**
	 * Constructs a new damage system.
	 */
	public DamageSystem() {
		super(new Signature(
				Health.class,
				Damage.class));
	}
	@Override
	public void attach() {
		events.register(ResetDamage.class, e -> entities.get(e.getId(), Damage.class).reset());
	}

	@Override
	public void update(int id, float dt) {
		Health health = entities.get(id, Health.class);
		Damage damage = entities.get(id, Damage.class);

		damage.apply(health, dt);

		if (health.isDead()) {
			events.enqueue(new DestroyEntity(id));
		}
	}
}
