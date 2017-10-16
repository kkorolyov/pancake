package dev.kkorolyov.pancake.platform.action;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.entity.Signature;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * An atomic alteration of an {@link Entity}.
 * All actions attached to an entity are applied at the beginning of a tick, before any game systems receive the entity.
 */
public abstract class Action implements Consumer<Entity> {
	private final Signature signature;

	/**
	 * @see #Action(Iterable)
	 */
	@SafeVarargs
	protected Action(Class<? extends Component>... componentTypes) {
		this(Arrays.asList(componentTypes));
	}
	/**
	 * Constructs a new action acting on entities containing the given components.
	 * @param componentTypes minimum component types required in accepted entities
	 */
	protected Action(Iterable<Class<? extends Component>> componentTypes) {
		this.signature = new Signature(componentTypes);
	}

	/**
	 * Alters an entity in some way.
	 * @param entity entity to alter
	 */
	protected abstract void apply(Entity entity);

	/**
	 * Applies this action to an entity if this action's signature is contained by the entity's.
	 * Otherwise, does nothing.
	 */
	@Override
	public void accept(Entity entity) {
		if (entity.contains(signature)) apply(entity);
	}
}
