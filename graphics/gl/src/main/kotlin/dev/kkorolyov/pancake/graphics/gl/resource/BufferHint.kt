package dev.kkorolyov.pancake.graphics.gl.resource

import org.lwjgl.opengl.GL46.*

/**
 * Provides a hint to how an `OpenGL` buffer object will be used.
 */
data class BufferHint(
	/**
	 * How frequently the buffer data will change.
	 */
	val frequency: Frequency,
	/**
	 * What the buffer data will be used for.
	 */
	val usage: Usage
) {
	internal val value: Int = when (frequency) {
		Frequency.STATIC -> when (usage) {
			Usage.DRAW -> GL_STATIC_DRAW
			Usage.READ -> GL_STATIC_READ
			Usage.COPY -> GL_STATIC_COPY
		}

		Frequency.DYNAMIC -> when (usage) {
			Usage.DRAW -> GL_DYNAMIC_DRAW
			Usage.READ -> GL_DYNAMIC_READ
			Usage.COPY -> GL_DYNAMIC_COPY
		}

		Frequency.STREAM -> when (usage) {
			Usage.DRAW -> GL_STREAM_DRAW
			Usage.READ -> GL_STREAM_READ
			Usage.COPY -> GL_STREAM_COPY
		}
	}

	/**
	 * Buffer change frequency.
	 */
	enum class Frequency {
		/**
		 * Data is set once.
		 */
		STATIC,
		/**
		 * Data is changed occasionally.
		 */
		DYNAMIC,
		/**
		 * Data is changed constantly.
		 */
		STREAM
	}
	/**
	 * Buffer usage.
	 */
	enum class Usage {
		/**
		 * Data is solely written.
		 */
		DRAW,
		/**
		 * Data is solely read.
		 */
		READ,
		/**
		 * Data is neither written nor read.
		 */
		COPY
	}
}
