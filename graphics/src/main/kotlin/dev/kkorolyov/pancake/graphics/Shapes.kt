package dev.kkorolyov.pancake.graphics

import dev.kkorolyov.pancake.graphics.resource.VertexBuffer
import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.math.Vector3
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Applies 4 vertices to this buffer forming a rectangle of [dimensions] and [color].
 * Vertex format: `[vec2 position, vec3 color]`.
 */
fun VertexBuffer.rectangle(dimensions: Vector2, color: Vector3) {
	val halfX = dimensions.x / 2
	val halfY = dimensions.y / 2

	add(Vector3.of(-halfX, -halfY, 0.0), color)
	add(Vector3.of(halfX, -halfY, 0.0), color)
	add(Vector3.of(halfX, halfY, 0.0), color)
	add(Vector3.of(-halfX, halfY, 0.0), color)
}

/**
 * Applies `pointCount + 1` vertices to this buffer forming an oval of [dimensions] with [pointCount] edge vertices.
 * Center vertex is colored [color] and edge vertices [edgeColor].
 * Vertex format: `[vec2 position, vec3 color]`.
 */
fun VertexBuffer.oval(dimensions: Vector2, color: Vector3, edgeColor: Vector3 = color, pointCount: Int = 50) {
	val pi2 = PI * 2

	add(Vector3.of(0.0, 0.0, 0.0), color)
	(0..pointCount).forEach {
		add(Vector3.of(dimensions.x / 2 * cos(it * pi2 / pointCount), dimensions.y / 2 * sin(it * pi2 / pointCount), 0.0), edgeColor)
	}
}
