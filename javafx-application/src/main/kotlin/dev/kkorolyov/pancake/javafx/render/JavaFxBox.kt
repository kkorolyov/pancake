package dev.kkorolyov.pancake.javafx.render

import dev.kkorolyov.pancake.platform.math.Vector
import dev.kkorolyov.pancake.platform.media.graphic.RenderTransform
import dev.kkorolyov.pancake.platform.media.graphic.shape.Box

/**
 * [Box] implemented through JavaFX.
 */
class JavaFxBox(private val unitPixels: Vector, private val g: EnhancedGraphicsContext) : Box() {
	private val finalSize: Vector = Vector()

	override fun render(transform: RenderTransform) {
		finalSize.apply {
			set(size)
			scale(unitPixels)
			scale(transform.scale)
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
