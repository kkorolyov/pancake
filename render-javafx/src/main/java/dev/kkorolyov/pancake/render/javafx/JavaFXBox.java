package dev.kkorolyov.pancake.render.javafx;

import dev.kkorolyov.pancake.platform.math.Vector;
import dev.kkorolyov.pancake.platform.media.graphic.RenderTransform;
import dev.kkorolyov.pancake.platform.media.graphic.shape.Box;

import javafx.scene.canvas.GraphicsContext;

/**
 * {@link Box} implemented through JavaFX.
 */
public class JavaFXBox extends Box {
	private final EnhancedGraphicsContext g;
	private final Vector finalSize = new Vector();

	/**
	 * Constructs a new JavaFX-backed box instance.
	 * @param g backing graphics context
	 */
	public JavaFXBox(EnhancedGraphicsContext g) {
		this.g = g;
	}

	@Override
	public void render(RenderTransform transform) {
		finalSize
				.set(getSize())
				.scale(transform.getScale());

		GraphicsContext gg = g
				.shape(this)
				.rotate(transform)
				.get();

		gg.fillRect(
				transform.getPosition().getX(),
				transform.getPosition().getY(),
				finalSize.getX(),
				finalSize.getY()
		);
		gg.strokeRect(
				transform.getPosition().getX(),
				transform.getPosition().getY(),
				finalSize.getX(),
				finalSize.getY()
		);
	}
}
