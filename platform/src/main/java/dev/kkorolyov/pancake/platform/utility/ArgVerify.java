package dev.kkorolyov.pancake.platform.utility;

/**
 * Static argument verification methods.
 * Each method accepts an argument name, bounds, and actual value, and returns the value if matches requirements; else throws {@link IllegalArgumentException},
 */
public final class ArgVerify {
	private ArgVerify() {}

	/** @throws IllegalArgumentException if {@code value > bound} */
	public static long lessThanEqual(String name, long bound, long value) {
		if (value > bound) throw new IllegalArgumentException(name + " must be <= " + bound + "; was " + value);
		return value;
	}
	/** @throws IllegalArgumentException if {@code value < bound} */
	public static long greaterThanEqual(String name, long bound, long value) {
		if (value < bound) throw new IllegalArgumentException(name + " must be >= " + bound + "; was " + value);
		return value;
	}
	/** @throws IllegalArgumentException if {@code value < lower || value > upper} */
	public static long betweenInclusive(String name, long lower, long upper, long value) {
		if (value < lower || value > upper) throw new IllegalArgumentException(name + " must be >= " + lower + " and <= " + upper + "; was " + value);
		return value;
	}

	/** @throws IllegalArgumentException if {@code value > bound} */
	public static double lessThanEqual(String name, double bound, double value) {
		if (value > bound) throw new IllegalArgumentException(name + " must be <= " + bound + "; was " + value);
		return value;
	}
	/** @throws IllegalArgumentException if {@code value < bound} */
	public static double greaterThanEqual(String name, double bound, double value) {
		if (value < bound) throw new IllegalArgumentException(name + " must be >= " + bound + "; was " + value);
		return value;
	}
	/** @throws IllegalArgumentException if {@code value < lower || value > upper} */
	public static double betweenInclusive(String name, double lower, double upper, double value) {
		if (value < lower || value > upper) throw new IllegalArgumentException(name + " must be >= " + lower + " and <= " + upper + "; was " + value);
		return value;
	}
}
