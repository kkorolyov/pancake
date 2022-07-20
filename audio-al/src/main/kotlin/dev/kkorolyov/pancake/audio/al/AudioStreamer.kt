package dev.kkorolyov.pancake.audio.al

import dev.kkorolyov.pancake.audio.al.internal.alCall
import dev.kkorolyov.pancake.audio.al.internal.asAl
import dev.kkorolyov.pancake.platform.utility.ArgVerify
import org.lwjgl.openal.AL11.*
import org.lwjgl.system.MemoryStack
import org.slf4j.LoggerFactory
import java.io.InputStream
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem

private const val STREAM_SLEEP = 100L

private val log = LoggerFactory.getLogger(AudioStreamer::class.java)

/**
 * A stream of audio buffered in increments to an attached source.
 */
class AudioStreamer(
	/** Opens a new handle to an audio input stream. */
	streamGen: () -> InputStream,
	/** Maximum number of concurrent queued buffers per source. */
	private val bufferCount: Int = 4,
	/** Maximum bytes per queued buffer. */
	private val bufferSize: Int = 32 shl 10
) : AudioData, AutoCloseable {
	private val streamGen = { streamGen().buffered() }

	init {
		ArgVerify.greaterThan("bufferCount", 0, bufferCount)
		ArgVerify.greaterThan("bufferSize", 0, bufferSize)
	}

	private val executor = Executors.newSingleThreadExecutor(object : ThreadFactory {
		private val defaultFactory = Executors.defaultThreadFactory()

		override fun newThread(r: Runnable): Thread {
			val thread = defaultFactory.newThread(r)
			thread.isDaemon = true
			return thread
		}
	})

	private val sources = mutableMapOf<AudioSource, Runner>()

	override fun attach(source: AudioSource) {
		sources.getOrPut(source) {
			val runner = Runner(source)
			executor.execute(runner)
			runner
		}
	}

	override fun close() {
		executor.shutdownNow()
		sources.values.forEach(Runner::close)
	}

	private inner class Runner(private val source: AudioSource) : Runnable, AutoCloseable {
		private var stream: AudioInputStream = AudioSystem.getAudioInputStream(streamGen())

		// FIXME this
		override fun run() {
			while (true) {
				// initial load
				if (alCall { alGetSourcei(source.id, AL_BUFFERS_QUEUED) } == 0 && alCall { alGetSourcei(source.id, AL_BUFFERS_PROCESSED) } == 0) {
					for (i in 1..bufferCount) {
						val buffer = alCall(::alGenBuffers)
						val read = read(buffer)
						if (read > 0) alCall { alSourceQueueBuffers(source.id, buffer) }

						if (read < bufferSize) {
							stream.close()

							if (source.loop) stream = AudioSystem.getAudioInputStream(streamGen())
							else return
						}
					}
				}
				// subsequent buffering
				else {
					while (alCall { alGetSourcei(source.id, AL_BUFFERS_PROCESSED) } > 0) {
						val buffer = alCall { alSourceUnqueueBuffers(source.id) }
						val read = read(buffer)
						if (read > 0) alCall { alSourceQueueBuffers(source.id, buffer) }

						if (read < bufferSize) {
							stream.close()

							if (source.loop) stream = AudioSystem.getAudioInputStream(streamGen())
							else return
						}
					}
				}
				Thread.sleep(STREAM_SLEEP)
			}
		}

		private fun read(buffer: Int): Int {
			val bytes = ByteArray(bufferSize)
			val read = stream.read(bytes)

			if (read > 0) {
				log.debug("Read {} bytes for source {} buffer {}", read, source.id, buffer)

				MemoryStack.stackPush().use {
					alCall { alBufferData(buffer, stream.format.asAl().value, it.bytes(*bytes), stream.format.sampleRate.toInt()) }
				}
			}

			return read
		}

		override fun close() {
			source.stop()
			MemoryStack.stackPush().use {
				val bufferP = it.mallocInt(alCall { alGetSourcei(source.id, AL_BUFFERS_PROCESSED) })
				alCall { alSourceUnqueueBuffers(source.id, bufferP) }
				alCall { alDeleteBuffers(bufferP) }
			}
			stream.close()
		}
	}
}
