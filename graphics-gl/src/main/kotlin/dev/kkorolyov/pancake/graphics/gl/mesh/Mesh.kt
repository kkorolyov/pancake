package dev.kkorolyov.pancake.graphics.gl.mesh

import org.lwjgl.opengl.GL46.*

/**
 * A configuration of vertex data.
 */
class Mesh(
	private val id: Int,
	private val vCount: Int,
	private val iCount: Int,
	private val mode: Int,
	private val vOffset: Int = 0,
	iOffset: Int = 0
) {
	private val iOffset: Long = iOffset * Int.SIZE_BYTES.toLong()

	/**
	 * Returns a view of this mesh drawing [vCount] vertices starting at [vOffset].
	 */
	fun subVertex(vCount: Int, vOffset: Int): Mesh = Mesh(id, vCount, 0, mode, vOffset, 0)

	/**
	 * Returns a view of this mesh drawing [iCount] indices starting at [iOffset].
	 */
	fun subIndex(iCount: Int, iOffset: Int) = Mesh(id, 0, iCount, mode, 0, iOffset)

	/**
	 * Draws this mesh using the current `OpenGL` context.
	 */
	fun draw() {
		glBindVertexArray(id)
		if (iCount > 0) glDrawElements(mode, iCount, GL_UNSIGNED_INT, iOffset) else glDrawArrays(mode, vOffset, vCount)
	}

	companion object {
		/**
		 * Returns the mesh built from [init].
		 */
		fun build(init: Builder.() -> Unit): Mesh = Builder().apply(init).build()
	}

	class Builder {
		var vertexBuffer: VertexBuffer? = null
		var indices: IntArray? = null
		var mode: DrawMode = DrawMode.TRIANGLES

		fun build(): Mesh {
			val vao = glGenVertexArrays()
			glBindVertexArray(vao)

			vertexBuffer?.bind()

			indices?.let {
				glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, glGenBuffers())
				glBufferData(GL_ELEMENT_ARRAY_BUFFER, it, GL_STATIC_DRAW)
			}

			glBindVertexArray(0)
			glBindBuffer(GL_ARRAY_BUFFER, 0)
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)

			return Mesh(vao, vertexBuffer?.size ?: 0, indices?.size ?: 0, mode.value)
		}
	}

	/**
	 * Represents an `OpenGL` drawing mode.
	 */
	enum class DrawMode(
		/**
		 * `OpenGL` value represented by this mode.
		 */
		val value: Int
	) {
		TRIANGLES(GL_TRIANGLES),
		TRIANGLE_STRIP(GL_TRIANGLE_STRIP),
		TRIANGLE_FAN(GL_TRIANGLE_FAN)
	}
}
