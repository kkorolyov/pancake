package dev.kkorolyov.pancake.graphics.resource

import dev.kkorolyov.pancake.graphics.GraphicsResource
import dev.kkorolyov.pancake.platform.math.Vector2

/**
 * Buffers vertices to a graphics API.
 */
interface VertexBuffer : GraphicsResource {
	/**
	 * Number of vertices in this buffer.
	 */
	val size: Int

	/**
	 * Appends a vertex composed of [attributes] to this buffer.
	 */
	fun add(vararg attributes: Vector2)

	/**
	 * Activates this buffer on the current render state and applies any pending changes to the backing representation.
	 */
	fun activate()
	/**
	 * Deactivates this buffer on the current render state.
	 */
	fun deactivate()
}
