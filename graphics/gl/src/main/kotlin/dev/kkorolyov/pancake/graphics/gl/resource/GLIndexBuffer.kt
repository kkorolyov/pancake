package dev.kkorolyov.pancake.graphics.gl.resource

import dev.kkorolyov.pancake.graphics.gl.internal.Cache
import dev.kkorolyov.pancake.graphics.resource.IndexBuffer
import org.lwjgl.opengl.GL46.*
import org.lwjgl.system.MemoryStack

/**
 * An `OpenGL` index buffer that can be reused across shared contexts.
 */
class GLIndexBuffer(private val hint: BufferHint = BufferHint(BufferHint.Frequency.STATIC, BufferHint.Usage.DRAW), init: GLIndexBuffer.() -> Unit = {}) : IndexBuffer {
	private val indices = mutableListOf<Int>()
	private var changed = false

	private val cache = Cache {
		glGenBuffers()
	}

	override val id by cache
	override val size: Int
		get() = indices.size

	init {
		init()
	}

	override fun add(index: Int) {
		changed = true
		indices += index
	}

	override fun activate() {
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id)

		if (changed) {
			MemoryStack.stackPush().use { stack ->
				val indexP = stack.mallocInt(indices.size)
				indices.forEach(indexP::put)
				indexP.flip()

				glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexP, hint.value)
			}
		}
	}

	override fun deactivate() {
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
	}

	override fun close() {
		cache.invalidate(::glDeleteBuffers)
	}
}
