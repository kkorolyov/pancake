package dev.kkorolyov.pancake.graphics.gl.resource

import dev.kkorolyov.pancake.graphics.util.Cache
import dev.kkorolyov.pancake.graphics.resource.IndexBuffer
import dev.kkorolyov.pancake.graphics.resource.Mesh
import dev.kkorolyov.pancake.graphics.resource.Texture
import dev.kkorolyov.pancake.graphics.resource.VertexBuffer
import org.lwjgl.opengl.GL46.*

/**
 * An `OpenGL` mesh that draws from [vertexBuffer] using draw [mode].
 * If [indexBuffer] is provided, draw calls will apply to those indices instead of to [vertexBuffer] vertices directly.
 * Any provided [textures] will be bound to the matching-index texture unit.
 */
class GLMesh(
	private val vertexBuffer: VertexBuffer,
	private val indexBuffer: IndexBuffer? = null,
	textures: Collection<Texture> = listOf(),
	private val mode: Mode = Mode.TRIANGLES
) : Mesh {
	private val textures = textures.toList()

	private val cache = Cache {
		val id = glCreateVertexArrays()

		glVertexArrayVertexBuffer(id, 0, vertexBuffer.id, 0, vertexBuffer.structure.sum() * Float.SIZE_BYTES)
		indexBuffer?.let {
			glVertexArrayElementBuffer(id, it.id)
		}

		var offset = 0
		vertexBuffer.structure.forEachIndexed { i, length ->
			glEnableVertexArrayAttrib(id, i)
			glVertexArrayAttribFormat(id, i, length, GL_FLOAT, false, offset * Float.SIZE_BYTES)
			glVertexArrayAttribBinding(id, i, 0)

			offset += length
		}

		id
	}

	override val id: Int by cache

	override fun draw(offset: Int, count: Int?) {
		activate()

		indexBuffer?.let { buffer ->
			glDrawElements(mode.value, count ?: buffer.size, GL_UNSIGNED_INT, offset * Int.SIZE_BYTES.toLong())
		} ?: glDrawArrays(mode.value, offset, count ?: vertexBuffer.size)
	}

	override fun activate() {
		glBindVertexArray(id)

		textures.forEachIndexed { i, texture ->
			glBindTextureUnit(i, texture.id)
		}
	}

	override fun deactivate() {
		glBindVertexArray(0)

		textures.forEachIndexed { i, _ ->
			glBindTextureUnit(i, 0)
		}
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
