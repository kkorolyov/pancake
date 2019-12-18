package dev.kkorolyov.pancake.platform

import dev.kkorolyov.pancake.platform.math.Vector

import static dev.kkorolyov.simplespecs.SpecUtilities.randDouble
/**
 * Provides utility methods for Specs.
 */
class SpecUtilities {
	/** @return vector with randomized component values */
	static Vector randVector() {
		return new Vector(randDouble(Integer.MAX_VALUE), randDouble(Integer.MAX_VALUE), randDouble(Integer.MAX_VALUE))
	}

	/**
	 * Wraps a double in a {@code BigDecimal}.
	 * @param value double value to wrap
	 * @return {@code value} as a {@code BigDecimal}
	 */
	static BigDecimal bigDecimal(double value) {
		return BigDecimal.valueOf(value)
	}
}
