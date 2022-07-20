package dev.kkorolyov.pancake.platform.math;

/**
 * Provides functions for comparing floating-point values in a consistent manner.
 */
final class FloatOps {
	private FloatOps() {}

	/**
	 * Normalizes {@code -0.0} values to {@code 0.0}, else returns {@code val}.
	 */
	static double sanitize(double val) {
		return val == 0.0 ? 0 : val;
	}

	/**
	 * Returns whether {@code val} and {@code other} are near enough to consider equal for most computations.
	 */
	static boolean equals(double val, double other) {
		return Math.abs(val - other) < 1e-9;
	}
}
