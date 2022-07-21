package dev.kkorolyov.pancake.audio.al.internal

import dev.kkorolyov.pancake.audio.al.AudioBuffer
import org.lwjgl.openal.AL
import org.lwjgl.openal.AL11.*
import org.lwjgl.openal.ALC
import org.lwjgl.openal.ALC11.*
import org.slf4j.LoggerFactory
import java.nio.ByteBuffer
import java.nio.IntBuffer
import javax.sound.sampled.AudioFormat

private val log = LoggerFactory.getLogger("audio.util")

private val alLoader = ThreadLocal.withInitial {
	try {
		AL.getCapabilities()
	} catch (e: IllegalStateException) {
		log.warn("OpenAL capabilities not yet loaded on thread '${Thread.currentThread().name}`; loading now")
		val device = alcOpenDevice(null as ByteBuffer?)
		val capabilities = ALC.createCapabilities(device)
		alcMakeContextCurrent(alcCreateContext(device, null as IntBuffer?))
		AL.createCapabilities(capabilities)
	}
}

/**
 * Ensures `OpenAL` capabilities are loaded and context initialized on the current thread.
 * Calling this directly is usually not needed - instead prefer wrapping `OpenAL` calls in [alCall].
 */
fun alLoad() {
	alLoader.get()
}

/**
 * Ensures [alLoad], invokes [block], follows it up with an [alAssert], and returns the result of [block],
 */
inline fun <T> alCall(block: () -> T): T {
	alLoad()
	val result = block()
	alAssert()
	return result
}

/**
 * Throws an exception if previous `OpenAL` operation raised an error.
 */
fun alAssert() {
	when (alGetError()) {
		AL_INVALID_NAME -> throw IllegalArgumentException("Bad name/id passed to OpenAL function")
		AL_INVALID_ENUM -> throw IllegalArgumentException("Invalid enum passed to OpenAL function")
		AL_INVALID_VALUE -> throw IllegalArgumentException("Invalid value passed to OpenAL function")
		AL_INVALID_OPERATION -> throw IllegalStateException("Requested invalid OpenAL operation")
		AL_OUT_OF_MEMORY -> throw IllegalStateException("OpenAL out of memory")
	}
}

/**
 * Returns the matching `OpenAL` format for this format.
 */
fun AudioFormat.asAl(): AudioBuffer.Format =
	when (channels) {
		1 -> when (sampleSizeInBits) {
			8 -> AudioBuffer.Format.MONO8
			16 -> AudioBuffer.Format.MONO16
			else -> throw IllegalArgumentException("No OpenAL format for sample size: $sampleSizeInBits")
		}
		2 -> when (sampleSizeInBits) {
			8 -> AudioBuffer.Format.STEREO8
			16 -> AudioBuffer.Format.STEREO16
			else -> throw IllegalArgumentException("No OpenAL format for sample size: $sampleSizeInBits")
		}
		else -> throw IllegalArgumentException("No OpenAL format for channels: $channels")
	}
