package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.math.Vector3;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Maintains a sequence of steps to go through.
 */
public final class Path implements Component {
	private final Queue<Vector3> steps = new ArrayDeque<>();

	/**
	 * Constructs a new path with initial {@code steps} sequence.
	 */
	public Path(Vector3... steps) {
		for (Vector3 step : steps) add(step);
	}

	/**
	 * Adds {@code step} to the end of the step queue.
	 */
	public void add(Vector3 step) {
		steps.add(step);
	}

	/**
	 * Dequeues and returns the next step, or {@code null} if no more steps.
	 */
	public Vector3 get() {
		return steps.poll();
	}

	/**
	 * Returns whether this path has at least one more step remaining.
	 */
	public boolean hasNext() {
		return !steps.isEmpty();
	}
}
