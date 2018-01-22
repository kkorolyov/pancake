package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.Spawner;
import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.core.event.Spawn;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.entity.Signature;
import dev.kkorolyov.pancake.platform.event.CreateEntity;

/**
 * Spawns entity clones from spawner entities.
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
		events.register(Spawn.class, e -> {
			Spawner spawner = entities.get(e.getId(), Spawner.class);
			Transform transform = entities.get(e.getId(), Transform.class);

			Iterable<Component> clone = spawner.spawn(transform.getPosition());
			events.enqueue(new CreateEntity(clone));
		});
	}

	@Override
	public void update(int id, float dt) {
		Spawner spawner = entities.get(id, Spawner.class);
		Transform transform = entities.get(id, Transform.class);

		Iterable<Component> clone = spawner.spawn(transform.getPosition(), dt);
		if (clone != null) events.enqueue(new CreateEntity(clone));
	}
}
