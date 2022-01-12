package dev.kkorolyov.pancake.graphics.jfx.drawable

import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.math.Vectors
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

/**
 * An oval drawn according to [dimensions] and [color].
 */
class Oval(dimensions: Vector2, private val color: Color) : Drawable {
	private val dimensions = Vectors.create(dimensions)

	override fun invoke(context: GraphicsContext) {
		context.let {
			it.fill = color

			it.fillOval(
				// shift to upper left corner
				-dimensions.x / 2,
				-dimensions.y / 2,
				dimensions.x,
				dimensions.y
			)
		}
	}
}
