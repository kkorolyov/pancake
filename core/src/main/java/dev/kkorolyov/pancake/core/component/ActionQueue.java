package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.entity.Component;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

/**
 * A collection of queued actions to apply to containing entities.
 * The same instance can be shared between multiple entities, which will all get the same actions applied to them.
 */
public final class ActionQueue implements Component, Iterable<Action> {
	private final Queue<Action> actions = new ArrayDeque<>();

	/**
	 * Adds {@code action} to the end of this queue.
	 */
	public void add(Action action) {
		actions.add(action);
	}

	/**
	 * Clears all queued actions.
	 */
	public void clear() {
		actions.clear();
	}

	@Override
	public Iterator<Action> iterator() {
		return actions.iterator();
	}
}
