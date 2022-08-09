package dev.kkorolyov.pancake.graphics.gl.resource

import dev.kkorolyov.pancake.graphics.gl.internal.Cache
import dev.kkorolyov.pancake.graphics.resource.VertexBuffer
import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.math.Vector3
import org.lwjgl.opengl.GL46.*
import kotlin.math.max

private fun count(vector: Vector2) = when (vector) {
	is Vector3 -> 3
	else -> 2
}

/**
 * An `OpenGL` vertex buffer that can be reused across shared contexts.
 */
class GLVertexBuffer private constructor(override val structure: List<Int>, override val size: Int, data: FloatArray) : VertexBuffer {
	private val cache = Cache {
		val id = glCreateBuffers()
		glNamedBufferData(id, data, BufferHint(BufferHint.Frequency.STATIC, BufferHint.Usage.DRAW).value)
		id
	}

	override val id by cache

	override fun close() {
		cache.invalidate(::glDeleteBuffers)
	}

	companion object {
		/**
		 * Returns a vertex buffer configured according to [init].
		 */
		operator fun invoke(init: VertexBuffer.Builder.() -> Unit): GLVertexBuffer {
			val builder = Builder()
			builder.init()
			return builder.build()
		}
	}

	private class Builder : VertexBuffer.Builder {
		private val vertices = mutableListOf<Array<out Vector2>>()

		override fun add(vararg attributes: Vector2) {
			vertices += attributes
		}

		override fun build(): GLVertexBuffer {
			val structure = IntArray(vertices.maxOf(Array<out Vector2>::size)).apply {
				vertices.forEach { vertex ->
					vertex.forEachIndexed { i, attr ->
						set(i, max(get(i), count(attr)))
					}
				}
			}
			val data = vertices.flatMap { vertex ->
				structure.flatMapIndexed { i, length ->
					mutableListOf<Float>().apply {
						// serialize each element
						if (vertex.size > i) {
							val vector = vertex[i]

							add(vector.x.toFloat())
							add(vector.y.toFloat())

							if (vector is Vector3) add(vector.z.toFloat())

							// pad any remainder
							for (j in count(vector) until length) add(0F)
						} else {
							// pad the entire attribute
							for (j in 0 until length) add(0F)
						}
					}
				}
			}

			return GLVertexBuffer(structure.asList(), vertices.size, data.toFloatArray())
		}
	}
}
