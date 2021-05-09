package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.Spawner;
import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.platform.plugin.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.entity.Signature;
import dev.kkorolyov.pancake.platform.event.CreateEntity;
import dev.kkorolyov.pancake.platform.utility.Limiter;

/**
 * Spawns entity clones from spawner entities.
 */
public class SpawnSystem extends GameSystem {
	/**
	 * Constructs a new spawn system.
	 */
	public SpawnSystem() {
		super(
				new Signature(Spawner.class, Transform.class),
				Limiter.fromConfig(SpawnSystem.class)
		);
	}

	@Override
	public void update(Entity entity, long dt) {
		Spawner spawner = entity.get(Spawner.class);
		Transform transform = entity.get(Transform.class);

		Iterable<Component> clone = spawner.spawn(transform.getPosition(), dt);
		if (clone != null) enqueue(new CreateEntity(clone));
	}
}
