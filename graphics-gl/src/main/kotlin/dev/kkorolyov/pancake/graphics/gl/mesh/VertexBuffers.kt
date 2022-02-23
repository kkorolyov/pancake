package dev.kkorolyov.pancake.graphics.gl.mesh

import dev.kkorolyov.pancake.platform.math.Vector3
import dev.kkorolyov.pancake.platform.math.Vectors
import org.lwjgl.opengl.GL46.*
import org.lwjgl.system.MemoryStack

private val defaultColor: Vector3 = Vectors.create(0.0, 0.0, 0.0)

/**
 * Sets this builder's vertex buffer to a [ColorPoint] configured by [init].
 */
fun Mesh.Builder.colorPoints(init: ColorPoint.() -> Unit) {
	vertexBuffer = ColorPoint().apply(init)
}

/**
 * Buffers vertices with the attributes:
 * ```
 * 0 vec3f position
 * 1 vec3f color
 * ```
 */
class ColorPoint : VertexBuffer {
	private val positions: MutableList<Vector3> = mutableListOf()
	private val colors: MutableList<Vector3> = mutableListOf()

	/**
	 * Appends a vertex with [position] and [color].
	 */
	fun add(position: Vector3, color: Vector3 = defaultColor) {
		positions += position
		colors += color
	}

	override val size: Int
		get() = positions.size

	override fun bind(vbo: Int) {
		glBindBuffer(GL_ARRAY_BUFFER, vbo)

		if (positions.isNotEmpty()) {
			MemoryStack.stackPush().use {
				val vertexP = it.callocFloat(size * (3 + 3))

				val pIt = positions.iterator()
				val cIt = colors.iterator()
				while (pIt.hasNext() && cIt.hasNext()) {
					pIt.next().apply {
						vertexP.put(x.toFloat())
						vertexP.put(y.toFloat())
						vertexP.put(z.toFloat())
					}
					cIt.next().apply {
						vertexP.put(x.toFloat())
						vertexP.put(y.toFloat())
						vertexP.put(z.toFloat())
					}
				}

				vertexP.position(0)
				glBufferData(GL_ARRAY_BUFFER, vertexP, GL_STATIC_DRAW)
			}
		}

		glBindVertexBuffer(0, vbo, 0, (3 + 3) * Float.SIZE_BYTES)
		glEnableVertexAttribArray(0)
		glEnableVertexAttribArray(1)
		glVertexAttribFormat(0, 3, GL_FLOAT, false, 0)
		glVertexAttribFormat(1, 3, GL_FLOAT, false, 3 * Float.SIZE_BYTES)
		glVertexAttribBinding(0, 0)
		glVertexAttribBinding(1, 0)
	}
}