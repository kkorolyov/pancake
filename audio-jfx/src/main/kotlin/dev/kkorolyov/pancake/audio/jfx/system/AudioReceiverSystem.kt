package dev.kkorolyov.pancake.audio.jfx.system

import dev.kkorolyov.pancake.audio.jfx.ReceiverQueue
import dev.kkorolyov.pancake.audio.jfx.component.AudioReceiver
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.entity.Entity

/**
 * Adds discovered receivers to a shared `queue`.
 */
class AudioReceiverSystem(private val queue: ReceiverQueue) : GameSystem(AudioReceiver::class.java) {
	override fun before() {
		queue.value = null
	}

	override fun update(entity: Entity, dt: Long) {
		queue.value = entity
	}
}
