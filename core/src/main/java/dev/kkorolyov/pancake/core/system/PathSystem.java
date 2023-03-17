package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.Go;
import dev.kkorolyov.pancake.core.component.Path;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;

/**
 * Moves an entity sequentially through the steps in a {@link Path} by adding {@link Go} instances to it.
 */
public class PathSystem extends GameSystem {
	public PathSystem() {
		super(Path.class);
	}

	@Override
	protected void update(Entity entity, long dt) {
		Path path = entity.get(Path.class);

		if (path.hasNext() && entity.get(Go.class) == null) entity.put(new Go(path.next(), path.getStrength(), path.getBuffer()));
	}
}
