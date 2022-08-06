package dev.kkorolyov.pancake.graphics.gl

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
 * Buffers data to an `OpenGL` vertex buffer object.
 */
class VertexBuffer {
	private val vertices = mutableListOf<Array<out Vector2>>()

	/**
	 * Number of vertices buffered by this.
	 */
	val size: Int
		get() = vertices.size

	/**
	 * Adds a vertex composed of `attributes` to this buffer.
	 */
	fun add(vararg attributes: Vector2) {
		this.vertices += attributes
	}

	/**
	 * Binds and buffers current data into the vertex buffer object [vbo].
	 */
	operator fun invoke(vbo: Int) {
		if (vertices.isNotEmpty()) {
			// set the sizes of the largest attributes encountered in any vertex
			val attributeLengths = IntArray(vertices.maxOf(Array<out Vector2>::size))
			vertices.forEach { vertex ->
				vertex.forEachIndexed { i, attr ->
					attributeLengths[i] = max(attributeLengths[i], count(attr))
				}
			}
			val attributeSum = attributeLengths.sum()

			glBindBuffer(GL_ARRAY_BUFFER, vbo)

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
				glBufferData(GL_ARRAY_BUFFER, vertexP, GL_STATIC_DRAW)
			}

			glBindVertexBuffer(0, vbo, 0, attributeSum * Float.SIZE_BYTES)
			for (i in 0 until attributeLengths.size) {
				glEnableVertexAttribArray(i)
				glVertexAttribFormat(i, attributeLengths[i], GL_FLOAT, false, if (i == 0) 0 else attributeLengths[i - 1] * Float.SIZE_BYTES)
				glVertexAttribBinding(i, 0)
			}

			glBindBuffer(GL_ARRAY_BUFFER, 0)
		}
	}
}
