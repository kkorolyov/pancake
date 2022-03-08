package dev.kkorolyov.pancake.audio.al

import dev.kkorolyov.pancake.audio.al.internal.alCall
import dev.kkorolyov.pancake.audio.al.internal.asAl
import org.lwjgl.openal.AL11.*
import org.lwjgl.system.MemoryStack
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ShortBuffer
import java.text.Format
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem

/**
 * Represents an `OpenAL` buffer that can load audio data.
 * The backing `OpenAL` object is thread-local, so accessing this buffer from different threads will affect different `OpenAL` objects.
 */
class AudioBuffer : AutoCloseable {
	private val tlId = ThreadLocal.withInitial {
		alCall(::alGenBuffers)
	}

	/**
	 * ID of this buffer in the current thread context.
	 */
	val id: Int
		get() = tlId.get()

	/**
	 * Fills this buffer with the data in [stream].
	 */
	fun fill(stream: InputStream) {
		val stream = AudioSystem.getAudioInputStream(stream)
		MemoryStack.stackPush().use {
			val format = stream.format
			fill(format.asAl(), format.sampleRate.toInt(), it.bytes(*stream.readAllBytes()))
		}
	}

	/**
	 * Fills this buffer with [data] of [format] and sample [frequency].
	 */
	fun fill(format: Format, frequency: Int, data: ByteBuffer) {
		alCall { alBufferData(id, format.value, data, frequency) }
	}

	/**
	 * Fills this buffer with [data] of [format] and sample [frequency].
	 */
	fun fill(format: Format, frequency: Int, data: ShortBuffer) {
		alCall { alBufferData(id, format.value, data, frequency) }
	}

	/**
	 * Deletes this buffer in the current thread context.
	 * Subsequent access to this buffer in the current thread context will reinitialize it.
	 */
	override fun close() {
		alCall { alDeleteBuffers(id) }
		tlId.remove()
	}

	/**
	 * Represents an `OpenAL` format.
	 */
	enum class Format(
		/** Represented `OpenAL` value. */
		val value: Int
	) {
		MONO8(AL_FORMAT_MONO8),
		MONO16(AL_FORMAT_MONO16),
		STEREO8(AL_FORMAT_STEREO8),
		STEREO16(AL_FORMAT_STEREO16);

		companion object {
			/**
			 * Returns the format represented by the `OpenAL` [value].
			 */
			fun forValue(value: Int): Format = values()[value % AL_FORMAT_MONO8]
		}
	}
}
