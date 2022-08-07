package dev.kkorolyov.pancake.graphics.gl.resource

import dev.kkorolyov.pancake.graphics.gl.internal.Cache
import dev.kkorolyov.pancake.graphics.resource.IndexBuffer
import dev.kkorolyov.pancake.graphics.resource.Mesh
import dev.kkorolyov.pancake.graphics.resource.VertexBuffer
import org.lwjgl.opengl.GL46.*

/**
 * An `OpenGL` mesh that draws from [vertexBuffer] (optionally with [indexBuffer]) using draw [mode].
 */
class GLMesh(
	private val vertexBuffer: VertexBuffer,
	private val indexBuffer: IndexBuffer? = null,
	private val mode: Mode = Mode.TRIANGLES
) : Mesh {
	private val cache = Cache {
		val id = glGenVertexArrays()

		// bind to VAO
		glBindVertexArray(id)
		vertexBuffer.activate()
		indexBuffer?.activate()

		// clear state
		glBindVertexArray(0)
		vertexBuffer.deactivate()
		indexBuffer?.deactivate()

		id
	}

	override val id: Int
		get() = cache()

	override fun draw(offset: Int, count: Int?) {
		glBindVertexArray(id)

		indexBuffer?.let { buffer ->
			glDrawElements(mode.value, count ?: buffer.size, GL_UNSIGNED_INT, offset * Int.SIZE_BYTES.toLong())
		} ?: glDrawArrays(mode.value, offset, count ?: vertexBuffer.size)
	}

	override fun deactivate() {
		glBindVertexArray(0)
	}

	override fun close() {
		cache.invalidate(::glDeleteVertexArrays)
	}

	/**
	 * A drawing mode.
	 */
	enum class Mode(internal val value: Int) {
		TRIANGLES(GL_TRIANGLES),
		TRIANGLE_STRIP(GL_TRIANGLE_STRIP),
		TRIANGLE_FAN(GL_TRIANGLE_FAN)
	}
}
