package dev.kkorolyov.pancake.graphics.jfx.component

import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.graphics.jfx.renderable.Drawable
import javafx.scene.canvas.GraphicsContext
import javafx.scene.transform.Affine

/**
 * A swappable [Drawable] component.
 */
class Graphic(private var delegate: Drawable) : Component, Drawable {
	override fun draw(context: GraphicsContext, affine: Affine) {
		delegate.draw(context, affine)
	}
}
