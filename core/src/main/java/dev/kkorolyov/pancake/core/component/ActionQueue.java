package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.entity.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * A collection of queued actions to apply to containing entities.
 * The same instance can be shared between multiple entities, which will all get the same actions applied to them.
 */
public final class ActionQueue implements Component, Iterable<Action> {
	private final Collection<Action> actions = new ArrayList<>();
	private final Collection<Action> buffer = new ArrayList<>();
	private boolean locked;

	// incremented on each flush(), used to offset later additions so that they don't incorrectly preempt earlier ones that have been waiting
	private long delayShift;
	private final Queue<Countdown<Action>> delayed = new PriorityQueue<>(Comparator.comparing(t -> t.remaining + delayShift));

	/**
	 * Adds {@code action} to the end of this queue.
	 * If this is called after calling {@link #iterator}, but before calling {@link #flush(long)}, buffers the action instead, and only adds it at end of the next {@link #flush(long)}.
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
	 * Schedules {@code action} for adding to this queue after (approximately) {@code delay} {@code ns} has elapsed.
	 * Delayed actions are checked and potentially added during a {@link #flush(long)}.
	 */
	public void delay(Action action, long delay) {
		// any delays queued by actions shouldn't update with the current cycle's dt
		delayed.add(new Countdown<>(action, delay, locked));
	}

	/**
	 * Clears all queued actions, adds any buffered actions, and adds delayed actions ready after additional {@code dt} {@code ns} elapsed.
	 */
	public void flush(long dt) {
		locked = false;
		actions.clear();

		actions.addAll(buffer);
		buffer.clear();

		for (Countdown<Action> countdown : delayed) countdown.update(dt);
		while (!delayed.isEmpty() && delayed.peek().isReady()) actions.add(delayed.poll().value);
		delayShift = delayed.isEmpty() ? 0 : delayShift + dt;
	}

	/**
	 * Returns the number of queued actions.
	 */
	public int size() {
		return actions.size();
	}

	@Override
	public Iterator<Action> iterator() {
		locked = true;
		return actions.iterator();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ActionQueue other)) return false;
		return Objects.equals(actions, other.actions);
	}
	@Override
	public int hashCode() {
		return Objects.hashCode(actions);
	}

	private static final class Countdown<T> {
		final T value;
		long remaining;
		boolean delayUpdate;

		Countdown(T value, long remaining, boolean delayUpdate) {
			this.value = value;
			this.remaining = remaining;
			this.delayUpdate = delayUpdate;
		}

		void update(long dt) {
			if (delayUpdate) delayUpdate = false;
			else remaining -= dt;
		}

		boolean isReady() {
			return remaining <= 0;
		}
	}
}
