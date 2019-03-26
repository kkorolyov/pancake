package dev.kkorolyov.pancake.platform

import dev.kkorolyov.pancake.platform.math.Vector
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton

import static dev.kkorolyov.simplespecs.SpecUtilities.randFloat
import static dev.kkorolyov.simplespecs.SpecUtilities.randInt

/**
 * Provides utility methods for Specs.
 */
class SpecUtilities {
	/** @return random KeyCode */
	static KeyCode randKeyCode() {
		return KeyCode.values()[randInt(KeyCode.values().length)]
	}
	/** @return random MouseButton */
	static MouseButton randMouseButton() {
		return MouseButton.values()[randInt(MouseButton.values().length)]
	}

	/** @return vector with randomized component values */
	static Vector randVector() {
		return new Vector(randFloat(Integer.MAX_VALUE), randFloat(Integer.MAX_VALUE), randFloat(Integer.MAX_VALUE))
	}

	/**
	 * Wraps a float in a {@code BigDecimal}.
	 * @param value float value to wrap
	 * @return {@code value} as a {@code BigDecimal}
	 */
	static BigDecimal bigDecimal(float value) {
		return BigDecimal.valueOf(value)
	}
}
