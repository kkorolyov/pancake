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
	 * Appends [index] to this buffer.
	 */
	fun add(index: Int)

	/**
	 * Activates this buffer on the current render state and applies any pending changes to the backing representation.
	 */
	fun activate()
	/**
	 * Deactivates this buffer on the current render state.
	 */
	fun deactivate()
}
