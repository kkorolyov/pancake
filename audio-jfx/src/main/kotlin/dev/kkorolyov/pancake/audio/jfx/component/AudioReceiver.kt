package dev.kkorolyov.pancake.audio.jfx.component

import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.utility.ArgVerify

/**
 * Indicates that the owning entity receives spatial audio events.
 */
class AudioReceiver : Component {
	/**
	 * Volume scale of all received audio between `0` (silent) and `1` (full volume).
	 */
	var volume: Double = 1.0
		set(value) {
			field = ArgVerify.betweenInclusive("volume", 0.0, 1.0, value)
		}
}
