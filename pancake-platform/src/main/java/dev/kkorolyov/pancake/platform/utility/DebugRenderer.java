package dev.kkorolyov.pancake.platform.utility;

import dev.kkorolyov.pancake.platform.Config;
import dev.kkorolyov.pancake.platform.Resources;
import dev.kkorolyov.pancake.platform.media.graphic.RenderTransform;
import dev.kkorolyov.pancake.platform.media.graphic.shape.Shape;
import dev.kkorolyov.pancake.platform.media.graphic.shape.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * Renders debug information.
 */
public final class DebugRenderer {
	private static final Pattern ARG_DELIMITER = Pattern.compile(",\\s*");
	private static final int LINE_HEIGHT = 14;
	private static final Logger LOG = LoggerFactory.getLogger(DebugRenderer.class);

	private final RenderTransform renderTransform = new RenderTransform();

	/**
	 * Renders performance information.
	 * @param performanceCounter performance counter with information to render
	 */
	public void render(PerformanceCounter performanceCounter) {
		String[] args = ARG_DELIMITER.split(Config.get().getProperty("renderInfo"));
		if (args.length <= 0) return;

		Text text = Resources.RENDER_MEDIUM.getText();
		renderTransform.reset();

		Resources.RENDER_MEDIUM.invoke(() -> {
			for (String arg : args) {
				switch (arg) {
					case "fps":
						text
								.setValue("FPS: " + performanceCounter.getTps())
								.render(renderTransform);

						renderTransform.getPosition().translate(0, LINE_HEIGHT);
						break;
					case "usage":
						for (PerformanceCounter.Usage usage : performanceCounter.getUsages()) {
							text
									.setValue(usage.toString())
									.setStroke(usage.exceedsMax() ? Shape.Color.RED : Shape.Color.BLACK)
									.render(renderTransform);

							renderTransform.getPosition().translate(0, LINE_HEIGHT);
						}
						break;
					default:
						LOG.warn("Unknown renderInfo arg: {}", arg);
				}
			}
		});
	}
}
