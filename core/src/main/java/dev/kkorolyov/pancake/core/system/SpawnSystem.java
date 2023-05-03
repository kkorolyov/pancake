package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.Position;
import dev.kkorolyov.pancake.core.component.Spawner;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.entity.Entity;

/**
 * Spawns entity clones from spawner entities.
 * @deprecated application-specific, should be removed from core library
 */
@Deprecated
public final class SpawnSystem extends GameSystem {
	/**
	 * Constructs a new spawn system.
	 */
	public SpawnSystem() {
		super(Spawner.class, Position.class);
	}

	@Override
	protected void update(Entity entity, long dt) {
		Spawner spawner = entity.get(Spawner.class);
		Position position = entity.get(Position.class);

		Iterable<Component> clone = spawner.spawn(position.getValue(), dt);
		if (clone != null) clone.forEach(create()::put);
	}
}
