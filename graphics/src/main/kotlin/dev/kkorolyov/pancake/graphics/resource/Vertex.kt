package dev.kkorolyov.pancake.graphics.resource

import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.math.Vector3

/**
 * An immutable tuple of attributes represented by vectors.
 */
class Vertex(vararg attributes: Vector2) : Iterable<Vector2> {
	private val data = Array(attributes.size) {
		when (val attribute = attributes[it]) {
			is Vector3 -> Vector3.readOnly(attribute)
			else -> Vector2.readOnly(attribute)
		}
	}

	/**
	 * The number of attributes in this vertex.
	 */
	val size: Int by attributes::size

	/**
	 * Returns the attribute at `index`.
	 */
	operator fun get(index: Int): Vector2 = data[index]

	override fun iterator(): Iterator<Vector2> = data.iterator()
}
