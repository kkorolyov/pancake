package dev.kkorolyov.pancake.javafx.render

import dev.kkorolyov.pancake.platform.media.graphic.RenderTransform
import dev.kkorolyov.pancake.platform.media.graphic.shape.Text

/**
 * [Text] implemented through JavaFX.
 */
class JavaFxText(private val g: EnhancedGraphicsContext) : Text() {
	override fun render(transform: RenderTransform) {
		g.run {
			shape(this@JavaFxText)
			rotate(transform)

			get().strokeText(value, transform.position.x, transform.position.y)
		}
	}
}
