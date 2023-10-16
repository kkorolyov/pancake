package dev.kkorolyov.pancake.graphics.resource

import dev.kkorolyov.pancake.graphics.GraphicsResource

/**
 * Combines vertex and texture data.
 */
interface Mesh : GraphicsResource.Active {
	/**
	 * A list of bounding boxes encompassing this mesh.
	 * Represented as a [List] of [List] of [Pair]s, where outer list elements correspond to the attributes of this mesh's [VertexBuffer.structure],
	 * the inner list to the components of that specific attribute,
	 * and the pair to a `(min, max)` combination for that specific component
	 */
	val bounds: List<List<Pair<Double, Double>>>

	/**
	 * Activates and draws this mesh with the current render state.
	 * If set, draws only the vertices starting at [offset], else starting at the first vertex.
	 * If set, draws only [count] vertices, else all of them.
	 */
	fun draw(offset: Int = 0, count: Int? = null)
}
