package dev.kkorolyov.pancake.platform.math;

/**
 * A value between minimum and maximum bounds.
 */
public class BoundedValue<T extends Comparable<T>> {
	private T value;
	private T minimum, maximum;

	/**
	 * Constructs a new bounded value.
	 * @param minimum minimum value
	 * @param maximum maximum value
	 * @param value current value
	 */
	public BoundedValue(T minimum, T maximum, T value) {
		this.minimum = minimum;
		this.maximum = maximum;

		set(value);
	}

	/** @return current value */
	public T get() {
		return value;
	}
	/**
	 * Ensures that the new current value is within lower and upper bounds, if set.
	 * @param value new current value
	 * @return {@code this}
	 */
	public BoundedValue<T> set(T value) {
		this.value = nullCompare(value, minimum) < 0 ? minimum	:
				nullCompare(value, maximum) > 0 ? maximum : value;
		return this;
	}
	private int nullCompare(T value, T bound) {	// Counts null bounds as equal
		return bound == null ? 0 : value.compareTo(bound);
	}

	/** @return lower bound of this value */
	public T getMinimum() {
		return minimum;
	}
	/**
	 * @param minimum new lower bound of this value
	 * @return {@code this}
	 */
	public BoundedValue<T> setMinimum(T minimum) {
		this.minimum = minimum;
		return this;
	}

	/** @return upper bound of this value */
	public T getMaximum() {
		return maximum;
	}
	/**
	 * @param maximum new upper bound of this value
	 * @return {@code this}
	 */
	public BoundedValue<T> setMaximum(T maximum) {
		this.maximum = maximum;
		return this;
	}
}
