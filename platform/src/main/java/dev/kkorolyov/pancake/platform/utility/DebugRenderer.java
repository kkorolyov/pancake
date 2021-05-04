package dev.kkorolyov.pancake.platform.utility;

import dev.kkorolyov.pancake.platform.Config;
import dev.kkorolyov.pancake.platform.math.Vector2;
import dev.kkorolyov.pancake.platform.math.Vectors;
import dev.kkorolyov.pancake.platform.media.graphic.RenderTransform;
import dev.kkorolyov.pancake.platform.media.graphic.shape.Shape;
import dev.kkorolyov.pancake.platform.media.graphic.shape.Text;
import dev.kkorolyov.pancake.platform.service.RenderMedium;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * Renders debug information.
 */
public final class DebugRenderer {
	private static final Pattern ARG_DELIMITER = Pattern.compile(",\\s*");
	private static final Vector2 SHIFTER = Vectors.create(0, 14);
	private static final Logger LOG = LoggerFactory.getLogger(DebugRenderer.class);

	private final RenderMedium renderMedium;
	private final RenderTransform renderTransform = new RenderTransform();

	/**
	 * Constructs a new debug renderer.
	 * @param renderMedium render medium to use
	 */
	public DebugRenderer(RenderMedium renderMedium) {
		this.renderMedium = renderMedium;
	}

	/**
	 * Renders performance information.
	 * @param performanceCounter performance counter with information to render
	 */
	public void render(PerformanceCounter performanceCounter) {
		String[] args = ARG_DELIMITER.split(Config.get().getProperty("renderInfo"));
		if (args.length <= 0) return;

		Text text = renderMedium.getText();
		renderTransform.reset();
		renderTransform.getPosition().add(SHIFTER);

		renderMedium.invoke(() -> {
			for (String arg : args) {
				switch (arg) {
					case "fps":
						text.setValue("FPS: " + performanceCounter.getTps());
						text.render(renderTransform);

						renderTransform.getPosition().add(SHIFTER);
						break;
					case "usage":
						for (PerformanceCounter.Usage usage : performanceCounter.getUsages()) {
							text.setValue(usage.toString());
							text.setStroke(usage.exceedsMax() ? Shape.Color.RED : Shape.Color.GREEN);
							text.render(renderTransform);

							renderTransform.getPosition().add(SHIFTER);
						}
						break;
					default:
						LOG.warn("Unknown renderInfo arg: {}", arg);
				}
			}
		});
	}
}
