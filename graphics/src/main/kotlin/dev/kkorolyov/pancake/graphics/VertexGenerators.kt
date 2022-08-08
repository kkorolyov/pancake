package dev.kkorolyov.pancake.graphics

import dev.kkorolyov.pancake.platform.math.Vector2
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Generates vertices for a rectangle of [dimensions].
 * Invokes [block] with each generated vertex.
 */
fun rectangle(dimensions: Vector2, block: (position: Vector2, texCoord: Vector2) -> Unit) {
	val halfX = dimensions.x / 2
	val halfY = dimensions.y / 2

	block(Vector2.of(-halfX, -halfY), Vector2.of())
	block(Vector2.of(halfX, -halfY), Vector2.of(1.0))
	block(Vector2.of(halfX, halfY), Vector2.of(1.0, 1.0))
	block(Vector2.of(-halfX, halfY), Vector2.of(0.0, 1.0))
}

/**
 * Generates vertices for an ellipse of [dimensions] with [count] vertices along its circumference.
 * Invokes [block] with each generated vertex.
 */
fun ellipse(dimensions: Vector2, count: Int = 50, block: (position: Vector2, texCoord: Vector2) -> Unit) {
	val weight = PI * 2 / count

	block(Vector2.of(), Vector2.of(0.5, 0.5))
	(0..count).forEach {
		// halve the given dimensions up front
		val halfXNorm = cos(it * weight) / 2
		val halfYNorm = sin(it * weight) / 2
		block(Vector2.of(dimensions.x * halfXNorm, dimensions.y * halfYNorm), Vector2.of(halfXNorm + 0.5, halfYNorm + 0.5))
	}
}
