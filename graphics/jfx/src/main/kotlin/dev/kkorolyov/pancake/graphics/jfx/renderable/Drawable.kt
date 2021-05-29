package dev.kkorolyov.pancake.graphics.jfx.renderable

import javafx.scene.canvas.GraphicsContext
import javafx.scene.transform.Affine

/**
 * A flyweight graphical template able to draw its transform using a graphics context.
 */
interface Drawable {
	/**
	 * Draws the [affine] transformation of `this` using [context].
	 */
	fun draw(context: GraphicsContext, affine: Affine)
}
