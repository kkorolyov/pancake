package dev.kkorolyov.pancake.audio.al

import dev.kkorolyov.pancake.audio.al.internal.alCall
import dev.kkorolyov.pancake.platform.math.Vector3
import dev.kkorolyov.pancake.platform.math.Vectors
import org.lwjgl.openal.AL11.*
import org.lwjgl.system.MemoryStack

/**
 * Represents an `OpenAL` source that can play audio.
 * The backing `OpenAL` object is thread-local, so accessing this source from different threads will affect different `OpenAL` objects.
 */
class AudioSource private constructor(
	/**
	 * Initializer function invoked with source `id`.
	 */
	init: (Int) -> Unit,
	/**
	 * Cleanup function invoked with source `id`.
	 */
	private val close: (Int) -> Unit = {}
) : AutoCloseable {
	constructor(
		/** Buffer to play. */
		buffer: AudioBuffer
	) : this({ alSourcei(it, AL_BUFFER, buffer.id) })

	private val tlId = ThreadLocal.withInitial {
		val id = alCall(::alGenSources)

		alCall { init(id) }

		id
	}

	/**
	 * ID of this source in the current thread context.
	 */
	val id: Int
		get() = tlId.get()

	/**
	 * State of this source in the current thread context.
	 */
	val state: State
		get() = State.forValue(alCall { alGetSourcei(id, AL_SOURCE_STATE) })

	/**
	 * Position of this source in the current thread context.
	 */
	var position: Vector3
		get() = alCall {
			MemoryStack.stackPush().use {
				val pP = it.mallocFloat(3)
				alGetSourcefv(id, AL_POSITION, pP)
				Vectors.create(pP[0].toDouble(), pP[1].toDouble(), pP[2].toDouble())
			}
		}
		set(value) = alCall { alSource3f(id, AL_POSITION, value.x.toFloat(), value.y.toFloat(), value.z.toFloat()) }

	/**
	 * Velocity of this source in the current thread context.
	 */
	var velocity: Vector3
		get() = alCall {
			MemoryStack.stackPush().use {
				val vP = it.mallocFloat(3)
				alGetSourcefv(id, AL_VELOCITY, vP)
				Vectors.create(vP[0].toDouble(), vP[1].toDouble(), vP[2].toDouble())
			}
		}
		set(value) = alCall { alSource3f(id, AL_VELOCITY, value.x.toFloat(), value.y.toFloat(), value.z.toFloat()) }

	/**
	 * Whether this source loops continuously.
	 */
	var loop: Boolean
		get() = alCall { alGetSourcei(id, AL_LOOPING) == AL_TRUE }
		set(value) = alCall { alSourcei(id, AL_LOOPING, if (value) AL_TRUE else AL_FALSE) }

	/**
	 * Distance at which this source's volume is normally halved.
	 */
	var refDistance: Float
		get() = alCall { alGetSourcef(id, AL_REFERENCE_DISTANCE) }
		set(value) = alCall { alSourcef(id, AL_REFERENCE_DISTANCE, value) }

	/**
	 * Starts playback of this source in the current thread context.
	 * If currently [State.PAUSED], resumes from the current point, else from the start.
	 */
	fun play() {
		alCall { alSourcePlay(id) }
	}

	/**
	 * Pauses any playback of this source in the current thread context.
	 */
	fun pause() {
		alCall { alSourcePause(id) }
	}

	/**
	 * Halts any playback of this source in the current thread context.
	 */
	fun stop() {
		alCall { alSourceStop(id) }
	}

	/**
	 * Halts any playback and rewinds this source to the start in the current thread context.
	 */
	fun reset() {
		alCall { alSourceRewind(id) }
	}

	/**
	 * Deletes this source in the current thread context.
	 * Subsequent access to this source in the current thread context will reinitialize it.
	 */
	override fun close() {
		alCall { close(id) }
		alCall { alDeleteSources(id) }
		tlId.remove()
	}

	/**
	 * Represents an `OpenAL` source state.
	 */
	enum class State(
		/** Represented `OpenAL` value. */
		val value: Int
	) {
		/** Pending playback. */
		INITIAL(AL_INITIAL),

		/** Currently playing. */
		PLAYING(AL_PLAYING),

		/** Paused during playback. */
		PAUSED(AL_PAUSED),

		/** Playback ended. */
		STOPPED(AL_STOPPED);

		companion object {
			/**
			 * Returns the state represented by the `OpenAL` [value].
			 */
			fun forValue(value: Int): State = values()[value % AL_INITIAL]
		}
	}
}
