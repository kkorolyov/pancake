package dev.kkorolyov.pancake.system;

import dev.kkorolyov.pancake.GameSystem;
import dev.kkorolyov.pancake.component.Spawner;
import dev.kkorolyov.pancake.component.Transform;
import dev.kkorolyov.pancake.entity.Component;
import dev.kkorolyov.pancake.entity.Entity;
import dev.kkorolyov.pancake.entity.Signature;

import static dev.kkorolyov.pancake.event.CoreEvents.SPAWN;
import static dev.kkorolyov.pancake.event.PlatformEvents.CREATE;

/**
 * Spawns entity clones from spawner entities.
 * <pre>
 * Events received:
 * {@link dev.kkorolyov.pancake.event.CoreEvents#SPAWN} - spawns a clone using the provided entity's {@link Spawner} (Entity)
 *
 * Events emitted:
 * {@link dev.kkorolyov.pancake.event.PlatformEvents#CREATE} - when an entity is spawned (Entity)
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
		register(SPAWN, (Entity e) -> {
			Spawner spawner = e.get(Spawner.class);
			Transform transform = e.get(Transform.class);

			Iterable<Component> clone = spawner.spawn(transform.getPosition());
			enqueue(CREATE, clone);
		});
	}

	@Override
	public void update(Entity entity, float dt) {
		Spawner spawner = entity.get(Spawner.class);
		Transform transform = entity.get(Transform.class);

		Iterable<Component> clone = spawner.spawn(transform.getPosition(), dt);
		if (clone != null) enqueue(CREATE, clone);
	}
}
