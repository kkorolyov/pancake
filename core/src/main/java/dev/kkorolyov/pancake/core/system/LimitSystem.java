package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.limit.Limit;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.entity.Entity;

/**
 * Applies {@link Limit} constraints to components.
 */
public final class LimitSystem<T extends Component, L extends Limit<T>> extends GameSystem {
	private final Class<? extends T> componentC;
	private final Class<? extends L> limitC;

	/**
	 * Constructs a new limit system applying {@code limitC} component to {@code componentC} component.
	 */
	public LimitSystem(Class<? extends T> componentC, Class<? extends L> limitC) {
		super(componentC, limitC);

		this.componentC = componentC;
		this.limitC = limitC;
	}

	@Override
	protected void update(Entity entity, long dt) {
		entity.get(limitC).limit(entity.get(componentC));
	}
}
