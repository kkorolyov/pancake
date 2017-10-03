package dev.kkorolyov.killstreek.system;

import dev.kkorolyov.killstreek.component.Damage;
import dev.kkorolyov.killstreek.component.Health;
import dev.kkorolyov.killstreek.event.GameEvents;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.entity.Signature;
import dev.kkorolyov.pancake.platform.event.Events;

/**
 * Applies damage to entity health.
 * Removes dead entities.
 * <pre>
 * Events received:
 * {@link GameEvents#DAMAGE} - resets the provided entity's {@link Damage} (Entity)
 *
 * Events emitted:
 * {@link Events#DESTROY} - when an entity dies (Entity)
 * </pre>
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
		register(GameEvents.DAMAGE, (Entity e) -> e.get(Damage.class).reset());
	}

	@Override
	public void update(Entity entity, float dt) {
		Health health = entity.get(Health.class);
		Damage damage = entity.get(Damage.class);

		damage.apply(health, dt);

		if (health.isDead()) {
			enqueue(Events.DESTROY, entity);
		}
	}
}