package dev.kkorolyov.pancake.core.system.cleanup;

import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.entity.Entity;

import java.util.Arrays;

/**
 * Removes all provided ephemeral component types.
 */
public final class CleanupSystem extends GameSystem {
	private final Class<? extends Component>[] types;

	/**
	 * Constructs a new cleanup system for the component {@code signature}.
	 */
	@SafeVarargs
	public CleanupSystem(Class<? extends Component>... signature) {
		super(signature);
		types = Arrays.copyOf(signature, signature.length);
	}

	@Override
	protected void update(Entity entity, long dt) {
		entity.remove(types);
	}
}
