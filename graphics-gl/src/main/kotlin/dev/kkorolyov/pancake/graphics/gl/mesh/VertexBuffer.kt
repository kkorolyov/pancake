package dev.kkorolyov.pancake.graphics.gl.mesh

import org.lwjgl.opengl.GL46.*

/**
 * Binds and buffers data to an `OpenGL` vertex buffer object.
 */
interface VertexBuffer {
	/**
	 * Number of vertices in this buffer.
	 */
	val size: Int

	/**
	 * Generates, binds, and buffers this buffer's data into a new vertex buffer object and returns its ID.
	 */
	fun bind(): Int = glGenBuffers().apply(this::bind)

	/**
	 * Binds and buffers this buffer's data into the vertex buffer object [vbo].
	 */
	fun bind(vbo: Int)
}
