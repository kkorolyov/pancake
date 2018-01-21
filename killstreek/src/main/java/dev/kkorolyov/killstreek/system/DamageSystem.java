package dev.kkorolyov.killstreek.system;

import dev.kkorolyov.killstreek.component.Damage;
import dev.kkorolyov.killstreek.component.Health;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Signature;

import static dev.kkorolyov.killstreek.event.Events.DAMAGE;
import static dev.kkorolyov.pancake.platform.event.Events.DESTROY;

/**
 * Applies damage to entity health.
 * Removes dead entities.
 * <pre>
 * Events received:
 * {@link dev.kkorolyov.killstreek.event.Events#DAMAGE} - resets the provided entity's {@link Damage} (Entity)
 *
 * Events emitted:
 * {@link dev.kkorolyov.pancake.platform.event.Events#DESTROY} - when an entity dies (Entity)
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
		events.register(DAMAGE, (Integer id) -> entities.get(id, Damage.class).reset());
	}

	@Override
	public void update(int id, float dt) {
		Health health = entities.get(id, Health.class);
		Damage damage = entities.get(id, Damage.class);

		damage.apply(health, dt);

		if (health.isDead()) {
			events.enqueue(DESTROY, id);
		}
	}
}
