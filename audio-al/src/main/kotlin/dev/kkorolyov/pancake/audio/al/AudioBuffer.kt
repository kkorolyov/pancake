package dev.kkorolyov.pancake.audio.al

import dev.kkorolyov.pancake.audio.al.internal.alCall
import dev.kkorolyov.pancake.audio.al.internal.asAl
import org.lwjgl.openal.AL11.*
import org.lwjgl.system.MemoryStack
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ShortBuffer
import javax.sound.sampled.AudioSystem

/**
 * Represents an `OpenAL` buffer that can load audio data.
 * Can be attached to multiple sources.
 */
class AudioBuffer : AudioData, AutoCloseable {
	/**
	 * Buffer ID.
	 */
	val id: Int by lazy { alCall(::alGenBuffers) }

	private val sources = mutableSetOf<AudioSource>()

	/**
	 * Fills this buffer with the data in [stream].
	 */
	fun fill(stream: InputStream) {
		val stream = AudioSystem.getAudioInputStream(stream.buffered())
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

	override fun attach(source: AudioSource) {
		if (sources.add(source)) {
			alCall { alSourcei(source.id, AL_BUFFER, id) }
		}
	}

	/**
	 * Detaches from all known sources and deletes this buffer.
	 * Further operations on a closed buffer are undefined.
	 */
	override fun close() {
		sources.forEach {
			it.stop()
			alCall { alSourcei(it.id, AL_BUFFER, 0) }
		}
		alCall { alDeleteBuffers(id) }
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
