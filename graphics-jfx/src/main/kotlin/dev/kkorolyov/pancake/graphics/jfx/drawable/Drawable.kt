package dev.kkorolyov.pancake.graphics.jfx.drawable

import javafx.scene.canvas.GraphicsContext

/**
 * A flyweight graphical template able to draw its transform using a graphics context.
 */
interface Drawable {
	/**
	 * Draws `this` using [context].
	 */
	operator fun invoke(context: GraphicsContext)
}
