package dev.kkorolyov.pancake.graphics.resource

import dev.kkorolyov.pancake.graphics.GraphicsResource
import dev.kkorolyov.pancake.platform.math.Vector2

/**
 * Buffers vertices to a graphics API.
 */
interface VertexBuffer : GraphicsResource {
	/**
	 * This buffer's vertex structure - as the number of components per attribute.
	 * e.g. a structure of `[3, 4, 2, 3]` represents a vertex composed of `(vec3, vec4, vec2, vec3)`.
	 */
	val structure: List<Int>
	/**
	 * Number of vertices in this buffer.
	 */
	val size: Int

	/**
	 * A multi-step vertex buffer builder.
	 */
	interface Builder {
		/**
		 * Appends a vertex composed of [attributes] to this builder.
		 */
		fun add(vararg attributes: Vector2)

		/**
		 * Returns a vertex buffer from the current vertices added to this builder.
		 */
		fun build(): VertexBuffer
	}
}
