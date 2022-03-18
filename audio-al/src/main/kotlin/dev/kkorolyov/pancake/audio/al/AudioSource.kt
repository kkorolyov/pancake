package dev.kkorolyov.pancake.audio.al

import dev.kkorolyov.pancake.audio.al.internal.alCall
import dev.kkorolyov.pancake.platform.math.Vector3
import dev.kkorolyov.pancake.platform.math.Vectors
import org.lwjgl.openal.AL11.*
import org.lwjgl.system.MemoryStack
import java.io.InputStream
import java.util.concurrent.Executors

private const val STREAM_SLEEP = 100L

/**
 * Represents an `OpenAL` source that can play audio.
 */
class AudioSource : AutoCloseable {
	private val streamRunner = Executors.newSingleThreadExecutor {
		Thread(it).apply { isDaemon = true }
	}

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
				Vectors.create(pP[0].toDouble(), pP[1].toDouble(), pP[2].toDouble())
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
	 * Sets this source to play data from [buffer].
	 * If [buffer] is `null`, removes the current bound buffer.
	 */
	fun set(buffer: AudioBuffer?) {
		alCall { alSourcei(id, AL_BUFFER, buffer?.id ?: 0) }
	}

	/**
	 * Sets this source to stream data from [streamer].
	 */
	fun set(streamer: AudioStreamer) {
		streamRunner.execute {
			while (!streamer(this)) Thread.sleep(STREAM_SLEEP)
			// TODO close when stopped playing
		}
	}

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
