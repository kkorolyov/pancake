package dev.kkorolyov.pancake.audio.al

import dev.kkorolyov.pancake.audio.al.internal.alCall
import dev.kkorolyov.pancake.audio.al.internal.asAl
import dev.kkorolyov.pancake.platform.utility.ArgVerify
import org.lwjgl.openal.AL10
import org.lwjgl.openal.AL11.*
import org.lwjgl.system.MemoryStack
import org.slf4j.LoggerFactory
import java.io.InputStream
import java.nio.IntBuffer
import javax.sound.sampled.AudioSystem

private val log = LoggerFactory.getLogger(AudioStreamer::class.java)

/**
 * Incrementally queues audio data to a source.
 */
class AudioStreamer(
	/** Stream to queue. */
	stream: InputStream,
	/** Maximum number of concurrent queued buffers. */
	bufferCount: Int = 4,
	/** Maximum bytes per queued buffer. */
	bufferSize: Int = 32 shl 10
) : AutoCloseable {
	private val bufferSize = ArgVerify.greaterThan("bufferSize", 0, bufferSize)
	private val stream = AudioSystem.getAudioInputStream(stream)
	private val format = this.stream.format.asAl().value
	private val frequency = this.stream.format.sampleRate.toInt()

	private val buffers by lazy {
		val buffers = IntArray(bufferCount)
		alCall { alGenBuffers(buffers) }
		buffers
	}
	private var done = false

	init {
		ArgVerify.greaterThan("bufferCount", 0, bufferCount)
	}

	/**
	 * Queues next data chunk to [source], if more data exists and [source] has at least 1 buffer open.
	 * Returns `true` if all data has been queued.
	 */
	operator fun invoke(source: AudioSource): Boolean {
		// FIXME this is definitely simplifiabe
		if (!done) {
			val remaining = alCall { alGetSourcei(source.id, AL_BUFFERS_PROCESSED) }

			MemoryStack.stackPush().use {
				// first load
				if (alCall { alGetSourcei(source.id, AL_BUFFERS_QUEUED) } == 0) {
					queue(
						it.mallocInt(buffers.size).apply {
							buffers.forEach(this::put)
							flip()
						},
						source.id,
						it
					)
				} else if (remaining > 0) {
					queue(it.mallocInt(remaining).apply { alCall { alSourceUnqueueBuffers(source.id, this) } }, source.id, it)
				}
			}
		}
		return done
	}

	private fun queue(queue: IntBuffer, source: Int, stack: MemoryStack) {
		log.debug("Queueing {} bytes per {} buffers for source {}", bufferSize, buffers.size, source)

		alCall { alSourceQueueBuffers(source, read(queue, stack)) }
	}

	private fun read(queue: IntBuffer, stack: MemoryStack): IntBuffer {
		val bytes = ByteArray(bufferSize)
		(0 until queue.remaining()).forEach {
			val read = stream.read(bytes)

			log.debug("Read {} bytes for buffer {}", read, it)

			done = read < bytes.size

			if (read > 0) alCall { alBufferData(queue[it], format, stack.bytes(*if (done) bytes.copyOf(read) else bytes), frequency) }

			if (done) {
				log.debug("Processed all data")
				return stack.mallocInt(it + 1).apply {
					(0..it).forEach {
						put(queue[it])
					}
					flip()
				}
			}
		}
		return queue
	}

	/**
	 * Deletes all buffers used by this streamer.
	 * Further operations on a closed streamer are undefined.
	 */
	override fun close() {
		buffers.forEach { alCall { alDeleteBuffers(it) } }
	}
}
