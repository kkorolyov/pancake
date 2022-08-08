package dev.kkorolyov.pancake.graphics.gl.resource

import dev.kkorolyov.pancake.graphics.gl.internal.Cache
import dev.kkorolyov.pancake.graphics.resource.VertexBuffer
import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.math.Vector3
import org.lwjgl.opengl.GL46.*
import org.lwjgl.system.MemoryStack
import kotlin.math.max

private fun count(vector: Vector2) = when (vector) {
	is Vector3 -> 3
	else -> 2
}

/**
 * An `OpenGL` vertex buffer that can be reused across shared contexts.
 */
class GLVertexBuffer(private val hint: BufferHint = BufferHint(BufferHint.Frequency.STATIC, BufferHint.Usage.DRAW), init: GLVertexBuffer.() -> Unit = {}) : VertexBuffer {
	private val vertices = mutableListOf<Array<out Vector2>>()
	private var changed = false

	private val cache = Cache {
		glGenBuffers()
	}

	override val id by cache
	override val size: Int
		get() = vertices.size

	init {
		init()
	}

	override fun add(vararg attributes: Vector2) {
		changed = true
		vertices += attributes
	}

	override fun activate() {
		glBindBuffer(GL_ARRAY_BUFFER, id)

		if (changed) {
			changed = false

			// set the sizes of the largest attributes encountered in any vertex
			val attributeLengths = IntArray(vertices.maxOf(Array<out Vector2>::size))
			vertices.forEach { vertex ->
				vertex.forEachIndexed { i, attr ->
					attributeLengths[i] = max(attributeLengths[i], count(attr))
				}
			}
			val attributeSum = attributeLengths.sum()

			MemoryStack.stackPush().use { stack ->
				val vertexP = stack.mallocFloat(size * attributeSum)

				vertices.forEach { vertex ->
					attributeLengths.forEachIndexed { i, length ->
						if (vertex.size > i) {
							val vector = vertex[i]
							vertexP.put(vector.x.toFloat()).put(vector.y.toFloat())
							if (vector is Vector3) vertexP.put(vector.z.toFloat())

							// pad any remainder
							for (j in count(vector) until length) vertexP.put(0F)
						} else {
							// pad the entire attribute
							for (j in 0 until length) vertexP.put(0F)
						}
					}
				}
				vertexP.flip()

				glBufferData(GL_ARRAY_BUFFER, vertexP, hint.value)
			}

			var offset = 0
			glBindVertexBuffer(0, id, 0, attributeSum * Float.SIZE_BYTES)
			attributeLengths.forEachIndexed { i, length ->
				glEnableVertexAttribArray(i)
				glVertexAttribFormat(i, length, GL_FLOAT, false, offset * Float.SIZE_BYTES)
				glVertexAttribBinding(i, 0)

				offset += length
			}
		}
	}

	override fun deactivate() {
		glBindBuffer(GL_ARRAY_BUFFER, 0)
	}

	override fun close() {
		cache.invalidate(::glDeleteBuffers)
	}
}
