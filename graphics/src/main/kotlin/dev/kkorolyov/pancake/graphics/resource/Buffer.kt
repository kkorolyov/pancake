package dev.kkorolyov.pancake.graphics.resource

import dev.kkorolyov.pancake.platform.math.Vector3
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * A list of elements that is invalidated on change.
 */
abstract class Buffer<T>(vararg elements: T) : RenderResource(), Iterable<T> {
	protected val data: MutableList<T> = mutableListOf<T>().apply { addAll(elements) }

	/**
	 * The number of elements in this buffer.
	 */
	val size: Int by data::size

	/**
	 * The elements in this buffer serialized to bytes.
	 */
	abstract val bytes: ByteBuffer

	/**
	 * Returns the element at `index`.
	 */
	operator fun get(index: Int): T = data[index]
	/**
	 * Replaces the element at `index` with `element`.
	 */
	operator fun set(index: Int, element: T) {
		data[index] = element
		invalidate()
	}

	/**
	 * Adds [element] to the end of this buffer.
	 */
	fun add(element: T) {
		data.add(element)
		invalidate()
	}

	/**
	 * Removes and returns the element at [index].
	 */
	fun removeAt(index: Int): T {
		val result = data.removeAt(index)
		invalidate()
		return result
	}

	/**
	 * Clears all elements from this buffer.
	 */
	fun clear() {
		data.clear()
		invalidate()
	}

	override fun iterator(): Iterator<T> = data.iterator()

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Buffer<*>) return false

		if (data != other.data) return false

		return true
	}

	override fun hashCode(): Int {
		return data.hashCode()
	}

	companion object {
		fun vertex(vararg vertices: Vertex): Buffer<Vertex> = object : Buffer<Vertex>(*vertices) {
			override val bytes: ByteBuffer
				get() = ByteBuffer.allocateDirect(
					data.sumOf { vertex ->
						vertex.sumOf { attribute ->
							when (attribute) {
								is Vector3 -> 3 as Int
								else -> 2
							}
						}
					} * Float.SIZE_BYTES
				).order(ByteOrder.nativeOrder()).apply {
					data.forEach { vertex ->
						vertex.forEach { attribute ->
							putFloat(attribute.x.toFloat())
							putFloat(attribute.y.toFloat())
							(attribute as? Vector3)?.let { putFloat(it.z.toFloat()) }
						}
					}
					flip()
				}
		}

		fun int(vararg elements: Int): Buffer<Int> = object : Buffer<Int>(*elements.toTypedArray()) {
			override val bytes: ByteBuffer
				get() = ByteBuffer.allocateDirect(data.size * Int.SIZE_BYTES).order(ByteOrder.nativeOrder()).apply {
					data.forEach(::putInt)
					flip()
				}
		}
	}
}
