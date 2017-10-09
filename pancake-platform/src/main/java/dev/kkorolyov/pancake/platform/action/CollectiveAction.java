package dev.kkorolyov.pancake.platform.action;

import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.entity.Signature;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * An {@link Action} which applies a collection of other actions.
 */
public class CollectiveAction extends Action {
	private final Set<Action> actions = new HashSet<>();

	/**
	 * Constructs a new collective action.
	 * @param actions contained actions, duplicated are ignored
	 */
	public CollectiveAction(Action... actions) {
		this(Arrays.asList(actions));
	}
	/**
	 * Constructs a new collective action.
	 * @param actions contained actions, duplicates are ignored
	 */
	public CollectiveAction(Iterable<Action> actions) {
		super(new Signature());

		actions.forEach(this.actions::add);
	}

	/**
	 * Applies all contained, accepting actions to an entity.
	 */
	@Override
	protected void apply(Entity entity) {
		for (Action action : actions) action.accept(entity);
	}
}
