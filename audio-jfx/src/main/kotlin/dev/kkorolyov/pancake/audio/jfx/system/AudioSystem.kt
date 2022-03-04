package dev.kkorolyov.pancake.audio.jfx.system

import dev.kkorolyov.pancake.audio.jfx.ReceiverQueue
import dev.kkorolyov.pancake.audio.jfx.component.AudioEmitter
import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.platform.Config
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.math.Vector3
import dev.kkorolyov.pancake.platform.math.Vectors
import kotlin.math.max
import kotlin.math.min

/**
 * Maintains audio state relative to the current receiver.
 */
class AudioSystem(private val queue: ReceiverQueue) : GameSystem(AudioEmitter::class.java, Transform::class.java) {
	private var active = true

	private val emitPoint = Vectors.create3()

	override fun update(entity: Entity, dt: Long) {
		val emitter = entity.get(AudioEmitter::class.java)
		val transform = entity.get(Transform::class.java)

		emitter.active = active

		var volume = 1.0
		var balance = 0.0

		queue.position?.let {
			emitPoint.set(transform.position)
			emitPoint.add(it, -1.0)

			// reduce emitter position sensitivity by expanding a point to a sphere of some radius
			val audioRadius = Config.get(javaClass).getProperty("audioRadius").toDouble()

			volume = audioRadius / Vector3.magnitude(emitPoint)
			balance = emitPoint.x / audioRadius
		}

		emitter(
			queue.volume * min(1.0, volume),
			if (balance < 0) max(-1.0, balance) else min(1.0, balance)
		)
	}
}
