package dev.kkorolyov.pancake.javafx.render

import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.media.graphic.Image
import dev.kkorolyov.pancake.platform.media.graphic.RenderTransform
import javafx.scene.image.Image as FxImage

/**
 * [Image] implemented through JavaFX.
 */
class JavaFxImage(private val image: FxImage, private val g: EnhancedGraphicsContext) : Image() {
	override fun render(transform: RenderTransform) {
		val origin: Vector2 = viewport.getOrigin(image.width, image.height)
		val size: Vector2 = viewport.getSize(image.width, image.height)

		// Rotate
		g.run {
			rotate(transform, pad = true)

			get().drawImage(
				image,
				// Select source using viewport
				// Select source using viewport
				origin.x,
				origin.y,
				size.x,
				size.y,
				// Render using transform
				// Render using transform
				transform.position.x - (size.x / 2),
				transform.position.y - (size.y / 2),
				size.x * transform.scale.x,
				size.y * transform.scale.y
			)
		}
	}
}
