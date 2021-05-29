package dev.kkorolyov.pancake.javafx.render

import dev.kkorolyov.pancake.platform.math.Vector3
import dev.kkorolyov.pancake.platform.math.Vectors
import dev.kkorolyov.pancake.platform.media.graphic.RenderTransform
import dev.kkorolyov.pancake.platform.media.graphic.shape.Box

/**
 * [Box] implemented through JavaFX.
 */
class JavaFxBox(private val unitPixels: Double, private val g: EnhancedGraphicsContext) : Box() {
	private val finalSize: Vector3 = Vectors.create(0.0, 0.0, 0.0)

	override fun render(transform: RenderTransform) {
		finalSize.apply {
			set(size)
			scale(unitPixels)

			x *= transform.scale.x
			y *= transform.scale.y
		}

		g.run {
			shape(this@JavaFxBox)
			rotate(transform)

			get().run {
				fillRect(
					transform.position.x - (finalSize.x / 2),
					transform.position.y - (finalSize.y / 2),
					finalSize.x,
					finalSize.y
				)
				strokeRect(
					transform.position.x - (finalSize.x / 2),
					transform.position.y - (finalSize.y / 2),
					finalSize.x,
					finalSize.y
				)
			}
		}
	}
}
