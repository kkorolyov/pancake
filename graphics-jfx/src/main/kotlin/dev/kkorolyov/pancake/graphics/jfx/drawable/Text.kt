package dev.kkorolyov.pancake.graphics.jfx.drawable

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.text.TextAlignment

/**
 * Text drawn with [value] string at maximum [width] with [color].
 */
class Text(private val value: String, private val width: Double, private val color: Color) : Drawable {
	override fun invoke(context: GraphicsContext) {
		context.let {
			val transform = it.transform
			it.scale(1 / transform.mxx, 1 / transform.myy)

			it.textAlign = TextAlignment.CENTER
			it.fill = color

			it.fillText(value, 0.0, 0.0, width * transform.mxx)
		}
	}
}
