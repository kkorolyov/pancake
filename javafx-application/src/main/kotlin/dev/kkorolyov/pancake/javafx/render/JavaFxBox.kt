package dev.kkorolyov.pancake.javafx.render

import dev.kkorolyov.pancake.platform.math.Vector
import dev.kkorolyov.pancake.platform.media.graphic.RenderTransform
import dev.kkorolyov.pancake.platform.media.graphic.shape.Box

/**
 * [Box] implemented through JavaFX.
 */
class JavaFxBox(private val g: EnhancedGraphicsContext) : Box() {
	private val finalSize: Vector = Vector()

	override fun render(transform: RenderTransform) {
		finalSize
				.set(size)
				.scale(transform.scale)

		g.run {
			shape(this@JavaFxBox)
			rotate(transform)

			get().run {
				fillRect(
						transform.position.x,
						transform.position.y,
						finalSize.x,
						finalSize.y
				)
				strokeRect(
						transform.position.x,
						transform.position.y,
						finalSize.x,
						finalSize.y
				)
			}
		}
	}
}
