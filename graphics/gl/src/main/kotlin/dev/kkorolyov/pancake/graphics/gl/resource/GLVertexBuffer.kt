package dev.kkorolyov.pancake.graphics.gl.resource

import dev.kkorolyov.pancake.graphics.util.Cache
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
 * Initialized with `vertices`.
 */
class GLVertexBuffer(vararg vertices: Array<out Vector2>) : VertexBuffer {
	constructor(init: Builder.() -> Unit) : this(*Builder().apply(init).vertices.toTypedArray())

	override val structure: List<Int> = IntArray(vertices.maxOf(Array<out Vector2>::size)).apply {
		vertices.forEach { vertex ->
			vertex.forEachIndexed { i, attr ->
				set(i, max(get(i), count(attr)))
			}
		}
	}.asList()

	private val data = serialize(vertices)
	private val cache = Cache {
		val id = glCreateBuffers()
		val vertexBytes = (data.size * Float.SIZE_BYTES).toLong()

		glNamedBufferStorage(id, vertexBytes, GL_MAP_WRITE_BIT)

		glMapNamedBufferRange(id, 0, vertexBytes, GL_MAP_WRITE_BIT)!!.apply {
			data.forEach(::putFloat)
		}
		glUnmapNamedBuffer(id)

		id
	}

	override val id by cache
	override val size: Int = vertices.size

	private fun serialize(vertices: Array<out Array<out Vector2>>) = vertices.flatMap { vertex ->
		structure.flatMapIndexed { i, length ->
			mutableListOf<Float>().apply {
				// serialize each element
				if (vertex.size > i) {
					val vector = vertex[i]

					add(vector.x.toFloat())
					add(vector.y.toFloat())

					if (vector is Vector3) add(vector.z.toFloat())

					// pad any remainder
					for (j in count(vector) until length) add(0f)
				} else {
					// pad the entire attribute
					for (j in 0 until length) add(0f)
				}
			}
		}
	}.toFloatArray()

	override fun set(offset: Long, vararg vertices: Array<Vector2>) {
		val data = serialize(vertices)
		glMapNamedBufferRange(id, offset, (data.size * Float.SIZE_BYTES).toLong(), GL_MAP_WRITE_BIT)!!.apply {
			data.forEach(::putFloat)
		}
		glUnmapNamedBuffer(id)
	}

	override fun close() {
		cache.invalidate(::glDeleteBuffers)
	}

	/**
	 * Builds vertex buffers.
	 */
	class Builder {
		internal val vertices = mutableListOf<Array<out Vector2>>()

		/**
		 * Adds a vertex of [attributes] to this builder.
		 */
		fun add(vararg attributes: Vector2) {
			vertices += attributes
		}
	}
}
