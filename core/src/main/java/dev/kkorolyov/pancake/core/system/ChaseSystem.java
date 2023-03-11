package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.Chase;
import dev.kkorolyov.pancake.core.component.Position;
import dev.kkorolyov.pancake.core.component.movement.Force;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.math.Vector3;

/**
 * Alters an entity's force to move its position towards its chased target's position.
 */
public final class ChaseSystem extends GameSystem {
	public ChaseSystem() {
		super(Chase.class, Position.class, Force.class);
	}

	@Override
	protected void update(Entity entity, long dt) {
		Chase chase = entity.get(Chase.class);
		Vector3 position = entity.get(Position.class).getValue();
		Vector3 force = entity.get(Force.class).getValue();

		chase.chase(position, force);
	}
}
