package dev.kkorolyov.pancake.graphics.resource

import dev.kkorolyov.pancake.graphics.GraphicsResource

/**
 * Buffers indices to a graphics API.
 */
interface IndexBuffer : GraphicsResource {
	/**
	 * Number of indices in this buffer.
	 */
	val size: Int

	/**
	 * Returns the [i]th index.
	 * [i] must be in the range `[0, size)`
	 */
	fun get(i: Int): Int
}
