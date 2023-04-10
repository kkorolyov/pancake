package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.utility.ArgVerify;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;
import java.util.function.Predicate;

/**
 * Maintains a sequence of steps to go through.
 */
public final class Path implements Component, Iterable<Vector3>, Iterator<Vector3> {
	private double strength;
	private double proximity;
	private SnapStrategy snapStrategy;
	private final Queue<Vector3> steps = new ArrayDeque<>();

	/**
	 * Constructs a new path with {@code strength}, {@code proximity}, and {@code snapStrategy} to apply to steps.
	 */
	public Path(double strength, double proximity, SnapStrategy snapStrategy) {
		setStrength(strength);
		setProximity(proximity);
		this.snapStrategy = snapStrategy;
	}

	/**
	 * Adds {@code step} to the end of the step queue.
	 * Adds {@code step} as a reference, so subsequent modifications to it will be reflected by this path.
	 */
	public void add(Vector3 step) {
		steps.add(step);
	}

	/**
	 * Clears the step queue.
	 */
	public void clear() {
		steps.clear();
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
	 * Returns the radius around any step's target to consider as having reached the target.
	 */
	public double getProximity() {
		return proximity;
	}
	/**
	 * Sets the radius around any step's target to consider as having reached the target to {@code buffer}.
	 */
	public void setProximity(double proximity) {
		this.proximity = ArgVerify.greaterThanEqual("buffer", 0.0, proximity);
	}

	/**
	 * Returns the strategy used to determine whether the last step returned from a {@link Path#next()} call should end in a snap to target.
	 */
	public SnapStrategy getSnapStrategy() {
		return snapStrategy;
	}
	/**
	 * Sets the strategy used to determine whether the last step returned from a {@link Path#next()} call should end in a snap to target to {@code snapStrategy}.
	 */
	public void setSnapStrategy(SnapStrategy snapStrategy) {
		this.snapStrategy = snapStrategy;
	}

	/**
	 * Returns whether the owning entity's position should be set to exactly this component's target once it is within proximity.
	 */
	public boolean isSnap() {
		return snapStrategy.apply(this);
	}

	/**
	 * Dequeues and returns the next step.
	 * Throws {@link java.util.NoSuchElementException} if no more steps.
	 */
	@Override
	public Vector3 next() {
		return steps.remove();
	}

	/**
	 * Returns whether this path has at least one more step remaining.
	 */
	@Override
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

	/**
	 * Determines whether the last step returned from a {@link Path#next()} call is meant to end in a snap to target.
	 */
	public enum SnapStrategy {
		ALL(path -> true),
		NONE(path -> false),
		LAST(path -> !path.hasNext());

		private final Predicate<? super Path> test;

		SnapStrategy(Predicate<? super Path> test) {
			this.test = test;
		}

		private boolean apply(Path path) {
			return test.test(path);
		}
	}
}
