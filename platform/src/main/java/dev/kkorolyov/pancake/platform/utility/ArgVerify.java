package dev.kkorolyov.pancake.platform.utility;

/**
 * Static argument verification methods.
 * Each method accepts an argument name, bounds, and actual value, and returns the value if matches requirements; else throws {@link IllegalArgumentException},
 */
public final class ArgVerify {
	private ArgVerify() {}

	/** @throws IllegalArgumentException if {@code value >= bound} */
	public static <T extends Comparable<T>> T lessThan(String name, T bound, T value) {
		if (value.compareTo(bound) < 0) return value;
		throw new IllegalArgumentException(name + " must be < " + bound + "; was " + value);
	}
	/** @throws IllegalArgumentException if {@code value > bound} */
	public static <T extends Comparable<T>> T lessThanEqual(String name, T bound, T value) {
		if (value.compareTo(bound) <= 0) return value;
		throw new IllegalArgumentException(name + " must be <= " + bound + "; was " + value);
	}

	/** @throws IllegalArgumentException if {@code value =< bound} */
	public static <T extends Comparable<T>> T greaterThan(String name, T bound, T value) {
		if (value.compareTo(bound) > 0) return value;
		throw new IllegalArgumentException(name + " must be > " + bound + "; was " + value);
	}
	/** @throws IllegalArgumentException if {@code value < bound} */
	public static <T extends Comparable<T>> T greaterThanEqual(String name, T bound, T value) {
		if (value.compareTo(bound) >= 0) return value;
		throw new IllegalArgumentException(name + " must be >= " + bound + "; was " + value);
	}

	/** @throws IllegalArgumentException if {@code value < lower || value > upper} */
	public static <T extends Comparable<T>> T betweenInclusive(String name, T lower, T upper, T value) {
		if (value.compareTo(lower) >= 0 && value.compareTo(upper) <= 0) return value;
		throw new IllegalArgumentException(name + " must be >= " + lower + " and <= " + upper + "; was " + value);
	}
}
