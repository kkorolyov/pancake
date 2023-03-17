package dev.kkorolyov.pancake.core.system.limit;

import dev.kkorolyov.pancake.core.component.limit.Limit;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.entity.Entity;

/**
 * Applies {@link Limit} constraints to components.
 */
public final class LimitSystem<T extends Component, L extends Limit<T>> extends GameSystem {
	private final Class<T> componentC;
	private final Class<L> limitC;

	/**
	 * Constructs a new limit system
	 */
	public LimitSystem(Class<T> componentC, Class<L> limitC) {
		super(componentC, limitC);

		this.componentC = componentC;
		this.limitC = limitC;
	}

	@Override
	protected void update(Entity entity, long dt) {
		entity.get(limitC).limit(entity.get(componentC));
	}
}
