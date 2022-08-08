package dev.kkorolyov.pancake.graphics.resource

import dev.kkorolyov.pancake.graphics.GraphicsResource
import dev.kkorolyov.pancake.platform.math.Vector2

/**
 * Buffers vertices to a graphics API.
 */
interface VertexBuffer : GraphicsResource.Active {
	/**
	 * Number of vertices in this buffer.
	 */
	val size: Int

	/**
	 * Appends a vertex composed of [attributes] to this buffer.
	 */
	fun add(vararg attributes: Vector2)
}
