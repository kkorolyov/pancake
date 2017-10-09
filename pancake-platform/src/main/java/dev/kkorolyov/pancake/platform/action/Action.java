package dev.kkorolyov.pancake.platform.action;

import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.entity.Signature;

import java.util.function.Consumer;

/**
 * An atomic alteration of an {@link Entity}.
 * All actions attached to an entity are applied at the beginning of a tick, before any game systems receive the entity.
 */
public abstract class Action implements Consumer<Entity> {
	private final Signature signature;

	/**
	 * Constructs a new action acting on entities containing the provided signature.
	 * @param signature signature defining minimum components required for action function
	 */
	protected Action(Signature signature) {
		this.signature = signature;
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
