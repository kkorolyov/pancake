package dev.kkorolyov.pancake.graphics.resource

import dev.kkorolyov.pancake.graphics.GraphicsResource

/**
 * Combines vertex and texture data.
 */
interface Mesh : GraphicsResource.Active {
	/**
	 * Activates and draws this mesh with the current render state.
	 * If set, draws only the vertices starting at [offset], else starting at the first vertex.
	 * If set, draws only [count] vertices, else all of them.
	 */
	fun draw(offset: Int = 0, count: Int? = null)
}
