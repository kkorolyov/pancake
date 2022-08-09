package dev.kkorolyov.pancake.graphics.gl.resource

import dev.kkorolyov.pancake.graphics.gl.internal.Cache
import dev.kkorolyov.pancake.graphics.resource.IndexBuffer
import org.lwjgl.opengl.GL46.*

/**
 * An `OpenGL` index buffer that can be reused across shared contexts.
 */
class GLIndexBuffer(vararg indices: Int) : IndexBuffer {
	private val cache = Cache {
		val id = glCreateBuffers()
		glNamedBufferData(id, indices, BufferHint(BufferHint.Frequency.STATIC, BufferHint.Usage.DRAW).value)
		id
	}

	override val id by cache
	override val size: Int = indices.size

	override fun close() {
		cache.invalidate(::glDeleteBuffers)
	}
}
