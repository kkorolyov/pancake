package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.utility.ArgVerify;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

/**
 * Maintains a sequence of steps to go through.
 */
public final class Path implements Component, Iterable<Vector3> {
	private double strength;
	private double buffer;
	private final Queue<Vector3> steps = new ArrayDeque<>();

	/**
	 * Constructs a new path with {@code strength} and {@code buffer} to apply to all steps.
	 */
	public Path(double strength, double buffer) {
		setStrength(strength);
		setBuffer(buffer);
		for (Vector3 step : steps) add(step);
	}

	/**
	 * Adds {@code step} to the end of the step queue.
	 * Adds {@code step} as a reference, so subsequent modifications to it will be reflected by this path.
	 */
	public void add(Vector3 step) {
		steps.add(step);
	}

	/**
	 * Returns the intended force magnitude to get to any step's target.
	 */
	public double getStrength() {
		return strength;
	}
	/**
	 * Sets the intended force magnitude to get to any step's target to {@code strength}.
	 */
	public void setStrength(double strength) {
		this.strength = ArgVerify.greaterThanEqual("strength", 0.0, strength);
	}

	/**
	 * Returns the distance around any step's target to consider as having reached the target.
	 */
	public double getBuffer() {
		return buffer;
	}
	/**
	 * Sets the distance around any step's target to consider as having reached the target to {@code buffer}.
	 */
	public void setBuffer(double buffer) {
		this.buffer = ArgVerify.greaterThanEqual("buffer", 0.0, buffer);
	}

	/**
	 * Dequeues and returns the next step, or {@code null} if no more steps.
	 */
	public Vector3 next() {
		return steps.poll();
	}

	/**
	 * Returns whether this path has at least one more step remaining.
	 */
	public boolean hasNext() {
		return !steps.isEmpty();
	}

	/**
	 * Returns an iterator of the current steps in order.
	 */
	@Override
	public Iterator<Vector3> iterator() {
		return steps.iterator();
	}
}
