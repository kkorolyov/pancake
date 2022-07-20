package dev.kkorolyov.pancake.graphics.gl.mesh

import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.math.Vector3
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Returns a mesh that draws a rectangle of [dimensions] and vertex [color].
 */
fun rectangle(dimensions: Vector2, color: Vector3): Mesh {
	val halfX = dimensions.x / 2
	val halfY = dimensions.y / 2

	return Mesh.vertex(
		ColorPoint().apply {
			add(Vector3.of(-halfX, -halfY, 0.0), color)
			add(Vector3.of(halfX, -halfY, 0.0), color)
			add(Vector3.of(halfX, halfY, 0.0), color)
			add(Vector3.of(-halfX, halfY, 0.0), color)
		},
		Mesh.DrawMode.TRIANGLE_FAN
	)
}

/**
 * Returns a mesh that draws an oval of [dimensions] with [pointCount] edge vertices, colors the center vertex with [color] and edge vertices with [edgeColor].
 */
fun oval(dimensions: Vector2, color: Vector3, edgeColor: Vector3 = color, pointCount: Int = 50): Mesh {
	val pi2 = PI * 2

	return Mesh.vertex(
		ColorPoint().apply {
			add(Vector3.of(0.0, 0.0, 0.0), color)
			(0..pointCount).forEach {
				add(Vector3.of(dimensions.x / 2 * cos(it * pi2 / pointCount), dimensions.y / 2 * sin(it * pi2 / pointCount), 0.0), edgeColor)
			}
		},
		Mesh.DrawMode.TRIANGLE_FAN
	)
}
