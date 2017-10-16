package dev.kkorolyov.pancake.platform.action;

import dev.kkorolyov.pancake.platform.entity.Entity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * An {@link Action} which applies a collection of contained, delegate actions.
 */
public class CollectiveAction extends Action {
	private final Set<Action> delegates = new HashSet<>();

	/** @see #CollectiveAction(Iterable)  */
	public CollectiveAction(Action... delegates) {
		this(Arrays.asList(delegates));
	}
	/**
	 * Constructs a new collective action.
	 * @param delegates contained actions, duplicates are ignored
	 */
	public CollectiveAction(Iterable<Action> delegates) {
		delegates.forEach(this.delegates::add);
	}

	/**
	 * Applies all contained, accepting actions to an entity.
	 */
	@Override
	protected void apply(Entity entity) {
		for (Action delegate : delegates) delegate.accept(entity);
	}
}
