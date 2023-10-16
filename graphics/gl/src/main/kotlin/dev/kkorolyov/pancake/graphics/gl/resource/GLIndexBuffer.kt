package dev.kkorolyov.pancake.graphics.gl.resource

import dev.kkorolyov.pancake.graphics.util.Cache
import dev.kkorolyov.pancake.graphics.resource.IndexBuffer
import org.lwjgl.opengl.GL46.*

/**
 * An `OpenGL` index buffer that can be reused across shared contexts.
 */
class GLIndexBuffer(private vararg val indices: Int) : IndexBuffer {
	private val cache = Cache {
		val id = glCreateBuffers()
		glNamedBufferData(id, indices, GL_STATIC_DRAW)
		id
	}

	override val id by cache
	override val size: Int = indices.size

	override fun get(i: Int): Int {
		if (i < 0 || i >= size) throw IndexOutOfBoundsException("i [$i] is not in the range [0, $size)")

		return indices[i]
	}

	override fun close() {
		cache.invalidate(::glDeleteBuffers)
	}
}
