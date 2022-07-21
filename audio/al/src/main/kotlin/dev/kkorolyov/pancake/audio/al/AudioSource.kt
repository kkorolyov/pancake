package dev.kkorolyov.pancake.audio.al

import dev.kkorolyov.pancake.audio.al.internal.alCall
import dev.kkorolyov.pancake.platform.math.Vector3
import org.lwjgl.openal.AL11.*
import org.lwjgl.system.MemoryStack

/**
 * Represents an `OpenAL` source that can play audio.
 */
class AudioSource : AutoCloseable {
	/**
	 * Source ID.
	 */
	val id: Int by lazy { alCall(::alGenSources) }

	/**
	 * Source state.
	 */
	val state: State
		get() = State.forValue(alCall { alGetSourcei(id, AL_SOURCE_STATE) })

	/**
	 * Source position.
	 */
	var position: Vector3
		get() = alCall {
			MemoryStack.stackPush().use {
				val pP = it.mallocFloat(3)
				alGetSourcefv(id, AL_POSITION, pP)
				Vector3.of(pP[0].toDouble(), pP[1].toDouble(), pP[2].toDouble())
			}
		}
		set(value) = alCall { alSource3f(id, AL_POSITION, value.x.toFloat(), value.y.toFloat(), value.z.toFloat()) }

	/**
	 * Source velocity.
	 */
	var velocity: Vector3
		get() = alCall {
			MemoryStack.stackPush().use {
				val vP = it.mallocFloat(3)
				alGetSourcefv(id, AL_VELOCITY, vP)
				Vector3.of(vP[0].toDouble(), vP[1].toDouble(), vP[2].toDouble())
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
	 * Current volume of this source.
	 */
	var gain: Float
		get() = alCall { alGetSourcef(id, AL_GAIN) }
		set(value) = alCall { alSourcef(id, AL_GAIN, value) }

	/**
	 * Distance at which this source's volume is normally halved.
	 */
	var refDistance: Float
		get() = alCall { alGetSourcef(id, AL_REFERENCE_DISTANCE) }
		set(value) = alCall { alSourcef(id, AL_REFERENCE_DISTANCE, value) }

	/**
	 * Starts playback of this source.
	 * If currently [State.PAUSED], resumes from the current point, else from the start.
	 */
	fun play() {
		alCall { alSourcePlay(id) }
	}

	/**
	 * Pauses any playback of this source.
	 */
	fun pause() {
		alCall { alSourcePause(id) }
	}

	/**
	 * Halts any playback of this source.
	 */
	fun stop() {
		alCall { alSourceStop(id) }
	}

	/**
	 * Halts any playback and rewinds this source to the start.
	 */
	fun reset() {
		alCall { alSourceRewind(id) }
	}

	/**
	 * Deletes this source.
	 * Further operations on a closed source are undefined.
	 */
	override fun close() {
		alCall { alDeleteSources(id) }
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
