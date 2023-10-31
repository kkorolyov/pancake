package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.entity.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * A collection of queued actions to apply to containing entities.
 * The same instance can be shared between multiple entities, which will all get the same actions applied to them.
 */
public final class ActionQueue implements Component, Iterable<Action> {
	private final Collection<Action> actions = new ArrayList<>();
	private final Collection<Action> buffer = new ArrayList<>();
	private boolean locked;

	/**
	 * Adds {@code action} to the end of this queue.
	 * If this is called after calling {@link #iterator}, but before calling {@link #clear()}, buffers the action instead, and only adds it at end of the next {@link #clear()}.
	 * This is to support predictable, consistent behavior where an action in this queue queues a new action.
	 */
	public void add(Action action) {
		(locked ? buffer : actions).add(action);
	}
	/**
	 * Removes {@code action} from this queue (or from its buffer, see {@link #add(Action)}), if it exists.
	 * Returns whether such an instance existed and was removed.
	 */
	public boolean remove(Action action) {
		return (locked ? buffer : actions).remove(action);
	}
	/**
	 * Returns whether this queue (or its buffer, see {@link #add(Action)}) contains {@code action}.
	 */
	public boolean contains(Action action) {
		return (locked ? buffer : actions).contains(action);
	}

	/**
	 * Clears all queued actions, then adds any buffered actions.
	 */
	public void clear() {
		locked = false;
		actions.clear();

		actions.addAll(buffer);
		buffer.clear();
	}

	@Override
	public Iterator<Action> iterator() {
		locked = true;
		return actions.iterator();
	}
}
