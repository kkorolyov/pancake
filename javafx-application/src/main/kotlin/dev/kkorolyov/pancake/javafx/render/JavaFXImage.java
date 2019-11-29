package dev.kkorolyov.pancake.javafx.render;

import dev.kkorolyov.pancake.platform.math.Vector;
import dev.kkorolyov.pancake.platform.media.graphic.Image;
import dev.kkorolyov.pancake.platform.media.graphic.RenderTransform;

/**
 * {@link Image} implemented through JavaFX.
 */
public class JavaFXImage extends Image {
	private final javafx.scene.image.Image image;
	private final EnhancedGraphicsContext g;

	/**
	 * Constructs a new JavaFX-backed image instance.
	 * @param image backing JavaFX image instance
	 * @param g backing graphics context
	 */
	public JavaFXImage(javafx.scene.image.Image image, EnhancedGraphicsContext g) {
		this.image = image;
		this.g = g;
	}

	@Override
	public void render(RenderTransform transform) {
		Vector origin = getViewport().getOrigin(image.getWidth(), image.getHeight());
		Vector size = getViewport().getSize(image.getWidth(), image.getHeight());

		// Rotate
		g
				.rotate(transform)
				.get()
				.drawImage(
						image,
						// Select source using viewport
						origin.getX(),
						origin.getY(),
						size.getX(),
						size.getY(),
						// Render using transform
						transform.getPosition().getX(),
						transform.getPosition().getY(),
						size.getX() * transform.getScale().getX(),
						size.getY() * transform.getScale().getY()
				);
	}
}
