package dev.kkorolyov.pancake.audio.jfx

import dev.kkorolyov.pancake.audio.jfx.component.AudioReceiver
import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.math.Vector3
import dev.kkorolyov.pancake.platform.utility.ArgVerify

/**
 * Mutable holder of current audio receiver.
 */
class ReceiverQueue {
	/**
	 * Current audio receiver.
	 */
	var value: Entity? = null

	/**
	 * Current receiver position, if any.
	 */
	val position: Vector3?
		get() = value?.get(Transform::class.java)?.globalPosition

	/**
	 * Current receiver volume.
	 */
	val volume: Double
		get() = value?.get(AudioReceiver::class.java)?.volume ?: 1.0
}
