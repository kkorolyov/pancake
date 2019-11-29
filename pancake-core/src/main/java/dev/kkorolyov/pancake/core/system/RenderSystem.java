package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.core.component.media.Graphic;
import dev.kkorolyov.pancake.platform.Config;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.Resources;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.entity.Signature;
import dev.kkorolyov.pancake.platform.media.graphic.RenderTransform;
import dev.kkorolyov.pancake.platform.media.graphic.shape.Shape;
import dev.kkorolyov.pancake.platform.media.graphic.shape.Text;
import dev.kkorolyov.pancake.platform.utility.Limiter;
import dev.kkorolyov.pancake.platform.utility.PerformanceCounter.Usage;
import dev.kkorolyov.simplelogs.Logger;

import java.util.Collection;
import java.util.HashSet;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import static java.util.stream.Collectors.toList;

/**
 * Renders all game entities.
 */
public class RenderSystem extends GameSystem {
	private static final int LINE_HEIGHT = 14;
	private static final Logger log = Config.getLogger(RenderSystem.class);

	private final RenderTransform renderTransform = new RenderTransform();

	private final NavigableMap<Double, Set<Entity>> drawBuckets = new TreeMap<>();

	/**
	 * Constructs a new render system.
	 */
	public RenderSystem() {
		super(
				new Signature(Transform.class, Graphic.class),
				Limiter.fromConfig(RenderSystem.class)
		);
	}

	@Override
	public void before(long dt) {
		for (Collection<Entity> bucket : drawBuckets.values()) bucket.clear();
	}

	@Override
	public void update(Entity entity, long dt) {
		drawBuckets.computeIfAbsent(entity.get(Transform.class).getGlobalPosition().getZ(), k -> new HashSet<>())
				.add(entity);
	}

	@Override
	public void after(long dt) {
		Collection<Entity> toDraw = drawBuckets.values().stream()
				.flatMap(Collection::stream)
				.collect(toList());

		Resources.RENDER_MEDIUM.clear();

		for (Entity entity : toDraw) {
			draw(
					entity.get(Transform.class),
					entity.get(Graphic.class)
			);
		}
		drawDebug();
	}
	private void draw(Transform transform, Graphic graphic) {
		graphic.render(
				renderTransform
						.reset()
						.setPosition(Resources.RENDER_MEDIUM.getCamera().getRelativePosition(transform.getGlobalPosition()))
						.setRotation(transform.getGlobalOrientation())
		);
	}

	// TODO Move to dedicated platform DebugRenderer
	private void drawDebug() {
		String[] args = Config.config().getArray("renderInfo");
		if (args == null) return;

		Text text = Resources.RENDER_MEDIUM.getText();
		renderTransform.reset();

		for (String arg : args) {
			switch (arg) {
				case "fps":
					text
							.setValue("FPS: " + resources.performanceCounter.getTps())
							.render(renderTransform);

					renderTransform.getPosition().translate(0, LINE_HEIGHT);
					break;
				case "usage":
					for (Usage usage : resources.performanceCounter.getUsages()) {
						text
								.setValue(usage.toString())
								.setStroke(usage.exceedsMax() ? Shape.Color.RED : Shape.Color.BLACK)
								.render(renderTransform);

						renderTransform.getPosition().translate(0, LINE_HEIGHT);
					}
					break;
				default:
					log.warning("Unknown renderInfo arg: {}", arg);
			}
		}
	}
}
