package dev.kkorolyov.pancake.graphics.gl.mesh

/**
 * Buffers data to an `OpenGL` vertex buffer object.
 */
interface VertexBuffer {
	/**
	 * Number of vertices buffered by this.
	 */
	val size: Int

	/**
	 * Binds and buffers current data into the vertex buffer object [vbo].
	 */
	operator fun invoke(vbo: Int)
}
