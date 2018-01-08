package dev.kkorolyov.pancake.platform.action;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.entity.EntityPool;
import dev.kkorolyov.pancake.platform.entity.Signature;

import java.util.Arrays;
import java.util.UUID;
import java.util.function.BiConsumer;

/**
 * An atomic alteration of an entity.
 * All actions attached to an entity are applied at the beginning of a tick, before any game systems receive the entity.
 */
public abstract class Action implements BiConsumer<UUID, EntityPool> {
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
	 * @param id ID of entity to alter
	 * @param entities current entity context
	 */
	protected abstract void apply(UUID id, EntityPool entities);

	/**
	 * Applies this action to an entity if this action's signature is contained by the entity's.
	 * Otherwise, does nothing.
	 */
	@Override
	public void accept(UUID id, EntityPool entities) {
		if (entities.contains(id, signature)) apply(id, entities);
	}
}
