package dev.kkorolyov.pancake.render.javafx;

import dev.kkorolyov.pancake.platform.media.graphic.Image;
import dev.kkorolyov.pancake.platform.media.graphic.RenderMedium;
import dev.kkorolyov.pancake.platform.media.graphic.shape.Box;
import dev.kkorolyov.pancake.platform.media.graphic.shape.Text;

import javafx.scene.canvas.Canvas;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link RenderMedium} implemented through JavaFX.
 */
public class JavaFXRenderMedium implements RenderMedium {
	private final Canvas canvas = new Canvas();
	private final EnhancedGraphicsContext g = new EnhancedGraphicsContext(canvas.getGraphicsContext2D());

	private final Map<String, javafx.scene.image.Image> imageCache = new ConcurrentHashMap<>();

	@Override
	public Image getImage(String uri) {
		return new JavaFXImage(imageCache.computeIfAbsent(uri, javafx.scene.image.Image::new), g);
	}

	@Override
	public Box getBox() {
		return new JavaFXBox(g);
	}
	@Override
	public Text getText() {
		return new JavaFXText(g);
	}

	@Override
	public void clear() {
		g.get().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}
}
