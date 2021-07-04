package dev.kkorolyov.pancake.audio.jfx.system

import dev.kkorolyov.pancake.audio.jfx.AddListener
import dev.kkorolyov.pancake.audio.jfx.Listener
import dev.kkorolyov.pancake.audio.jfx.RemoveListener
import dev.kkorolyov.pancake.audio.jfx.SetAudioState
import dev.kkorolyov.pancake.audio.jfx.component.AudioEmitter
import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.platform.Config
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.Signature
import dev.kkorolyov.pancake.platform.math.VectorMath
import dev.kkorolyov.pancake.platform.math.Vectors
import dev.kkorolyov.pancake.platform.utility.Limiter
import kotlin.math.max
import kotlin.math.min

/**
 * Maintains audio state positioned relative to the nearest listener.
 * [AddListener] adds an audio listener.
 * [RemoveListener] removes an audio listener.
 * [SetAudioState] events set all audio playback state.
 */
class AudioSystem : GameSystem(
	Signature(AudioEmitter::class.java, Transform::class.java),
	Limiter.fromConfig(AudioSystem::class.java)
) {
	private val listeners = mutableListOf<Listener>()
	private var active = true

	private var emitPoint = Vectors.create3()

	override fun attach() {
		register(AddListener::class.java) { listeners.add(it.listener) }
		register(RemoveListener::class.java) { listeners.remove(it.listener) }

		// TODO may be replaced by a generic "ActionOnAll" event
		register(SetAudioState::class.java) { active = it.active }
	}

	override fun update(entity: Entity, dt: Long) {
		val emitter = entity.get(AudioEmitter::class.java)
		val transform = entity.get(Transform::class.java)

		emitter.active = active

		listeners
			.minByOrNull { VectorMath.distance(it.position, transform.position) }
			?.let {
				// reduce emitter position sensitivity by expanding a point to a sphere of some radius
				val audioRadius = Config.get(javaClass).getProperty("audioRadius").toDouble()

				emitPoint.set(transform.position)
				emitPoint.add(it.position, -1.0)

				val volume = audioRadius / VectorMath.magnitude(emitPoint)
				val balance = emitPoint.x / audioRadius

				emitter(
					min(1.0, volume),
					if (balance < 0) max(-1.0, balance) else min(1.0, balance)
				)
			}
	}
}
