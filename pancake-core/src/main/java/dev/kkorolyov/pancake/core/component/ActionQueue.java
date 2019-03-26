package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.entity.Entity;

import java.util.ArrayDeque;
import java.util.Queue;

import static dev.kkorolyov.simplefuncs.stream.Iterables.append;
import static java.util.Collections.singleton;

/**
 * A collection of queued actions to apply to containing entities.
 */
public class ActionQueue implements Component {
	private final Queue<Action> actions = new ArrayDeque<>();

	/** @see #enqueue(Iterable) */
	public ActionQueue enqueue(Action action, Action... actions) {
		return enqueue(append(singleton(action), actions));
	}
	/**
	 * @param actions actions to add to this queue
	 * @return {@code this}
	 */
	public ActionQueue enqueue(Iterable<Action> actions) {
		actions.forEach(this.actions::add);
		return this;
	}

	/**
	 * Applies queued actions in FIFO order to {@code entity}.
	 * @param entity entity to apply queued actions to
	 */
	public void apply(Entity entity) {
		for (Action action : actions) {
			action.apply(entity);
		}
	}

	/**
	 * Clears all queued actions.
	 */
	public void clear() {
		actions.clear();
	}
}
