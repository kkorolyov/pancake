package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.Spawner;
import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.entity.Signature;

import static dev.kkorolyov.pancake.core.event.Events.SPAWN;
import static dev.kkorolyov.pancake.platform.event.Events.CREATE;

/**
 * Spawns entity clones from spawner entities.
 * <pre>
 * Events received:
 * {@link dev.kkorolyov.pancake.core.event.Events#SPAWN} - spawns a clone using the provided entity's {@link Spawner} (Entity)
 *
 * Events emitted:
 * {@link dev.kkorolyov.pancake.platform.event.Events#CREATE} - when an entity is spawned (Entity)
 * </pre>
 */
public class SpawnSystem extends GameSystem {
	/**
	 * Constructs a new spawn system.
	 */
	public SpawnSystem() {
		super(new Signature(Spawner.class,
												Transform.class));
	}
	@Override
	public void attach() {
		events.register(SPAWN, (Integer id) -> {
			Spawner spawner = entities.get(id, Spawner.class);
			Transform transform = entities.get(id, Transform.class);

			Iterable<Component> clone = spawner.spawn(transform.getPosition());
			events.enqueue(CREATE, clone);
		});
	}

	@Override
	public void update(int id, float dt) {
		Spawner spawner = entities.get(id, Spawner.class);
		Transform transform = entities.get(id, Transform.class);

		Iterable<Component> clone = spawner.spawn(transform.getPosition(), dt);
		if (clone != null) events.enqueue(CREATE, clone);
	}
}
