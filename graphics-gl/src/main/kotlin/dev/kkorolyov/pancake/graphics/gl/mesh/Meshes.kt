package dev.kkorolyov.pancake.graphics.gl.mesh

import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.math.Vector3
import dev.kkorolyov.pancake.platform.math.Vectors
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Returns a mesh that draws a rectangle using [dimensions] and vertex [color].
 */
fun Mesh.Companion.rectangle(dimensions: Vector2, color: Vector3) = build {
	val halfX = dimensions.x / 2
	val halfY = dimensions.y / 2

	colorPoints {
		add(Vectors.create(-halfX, -halfY, 0.0), color)
		add(Vectors.create(halfX, -halfY, 0.0), color)
		add(Vectors.create(halfX, halfY, 0.0), color)
		add(Vectors.create(-halfX, halfY, 0.0), color)
	}

	mode = Mesh.DrawMode.TRIANGLE_FAN
}

/**
 * Returns a mesh that draws an oval with [pointCount] edge vertices using [dimensions] as radii, colors the center vertex with [color] and edge vertices with [edgeColor].
 */
fun Mesh.Companion.oval(dimensions: Vector2, color: Vector3, edgeColor: Vector3 = color, pointCount: Int = 50) = build {
	val pi2 = PI * 2

	colorPoints {
		add(Vectors.create(0.0, 0.0, 0.0), color)
		(0..pointCount).forEach {
			add(Vectors.create(dimensions.x * cos(it * pi2 / pointCount), dimensions.y * sin(it * pi2 / pointCount), 0.0), edgeColor)
		}
	}

	mode = Mesh.DrawMode.TRIANGLE_FAN
}
