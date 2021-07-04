package dev.kkorolyov.pancake.audio.jfx

import dev.kkorolyov.pancake.platform.math.Vector3
import dev.kkorolyov.pancake.platform.utility.ArgVerify

/**
 * Determines playing audio relative position and volume.
 */
class Listener(
	/** Reference to the position of this listener */
	val position: Vector3,
	/** Volume scale of all received audio - constrained `[0.0, 1.0]` */
	volume: Double = 1.0
) {
	val volume = ArgVerify.betweenInclusive("volume", 0.0, 1.0, volume)
}
