package dev.kkorolyov.pancake.render.javafx;

import dev.kkorolyov.pancake.platform.media.graphic.RenderTransform;
import dev.kkorolyov.pancake.platform.media.graphic.shape.Text;

/**
 * {@link Text} implemented through JavaFX.
 */
public class JavaFXText extends Text {
	private final EnhancedGraphicsContext g;

	/**
	 * Constructs a new JavaFX-backed text instance.
	 * @param g backing graphics context
	 */
	public JavaFXText(EnhancedGraphicsContext g) {
		this.g = g;
	}

	@Override
	public void render(RenderTransform transform) {
		g
				.shape(this)
				.rotate(transform)
				.get()
				.strokeText(getValue(), transform.getPosition().getX(), transform.getPosition().getY());
	}
}
