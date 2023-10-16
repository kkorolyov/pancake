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
	 * Returns [vertex]'s [attribute]'s [component]'s value.
	 * [vertex] must be in the range `[0, size)`
	 * [attribute] must be in the range `[0, structure.size)`
	 * [component] must be in the range `[0, structure[attribute].size)`
	 */
	fun get(vertex: Int, attribute: Int, component: Int): Double

	/**
	 * Sets the vertices in this buffer to [vertices] starting at [offset].
	 * Each vertex must match [structure] and the number of vertices must be `<=` [size] - [offset].
	 */
	fun set(offset: Long, vararg vertices: Array<Vector2>)
	/**
	 * [set] with offset `0`.
	 */
	fun set(vararg vertices: Array<Vector2>) {
		set(0, *vertices)
	}
}
