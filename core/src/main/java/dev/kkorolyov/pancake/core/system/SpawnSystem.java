package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.Spawner;
import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.entity.Entity;

/**
 * Spawns entity clones from spawner entities.
 */
public class SpawnSystem extends GameSystem {
	/**
	 * Constructs a new spawn system.
	 */
	public SpawnSystem() {
		super(Spawner.class, Transform.class);
	}

	@Override
	public void update(Entity entity, long dt) {
		Spawner spawner = entity.get(Spawner.class);
		Transform transform = entity.get(Transform.class);

		Iterable<Component> clone = spawner.spawn(transform.getPosition(), dt);
		if (clone != null) clone.forEach(create()::put);
	}
}
