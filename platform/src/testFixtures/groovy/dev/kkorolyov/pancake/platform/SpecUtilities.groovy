package dev.kkorolyov.pancake.platform

import dev.kkorolyov.pancake.platform.math.Vector3

import java.util.concurrent.ThreadLocalRandom

/**
 * Provides utility methods for Specs.
 */
final class SpecUtilities {
	/** @return vector with randomized component values */
	static Vector3 randVector() {
		return Vector3.of(randDouble(Integer.MAX_VALUE), randDouble(Integer.MAX_VALUE), randDouble(Integer.MAX_VALUE))
	}

	static int randInt(int bound = Integer.MAX_VALUE) {
		return ThreadLocalRandom.current().nextInt(bound)
	}
	static long randLong(long bound = Long.MAX_VALUE) {
		return ThreadLocalRandom.current().nextLong(bound)
	}
	static double randDouble(double bound = Double.MAX_VALUE) {
		return ThreadLocalRandom.current().nextDouble(bound)
	}

	static String randString() {
		return UUID.randomUUID().toString().replaceAll("-", "")
	}
}
