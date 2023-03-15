package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.Go;
import dev.kkorolyov.pancake.core.component.Path;
import dev.kkorolyov.pancake.core.component.Position;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.math.Vector3;

/**
 * Moves an entity {@link Position} sequentially through the steps in a {@link Path} by modifying its current {@link Go#getTarget()}.
 */
public class PathSystem extends GameSystem {
	public PathSystem() {
		super(Path.class, Go.class, Position.class);
	}

	@Override
	protected void update(Entity entity, long dt) {
		Position position = entity.get(Position.class);
		Go go = entity.get(Go.class);
		Path path = entity.get(Path.class);

		if (path.hasNext() && Vector3.distance(position.getValue(), go.getTarget()) <= go.getBuffer()) go.getTarget().set(path.get());
	}
}
