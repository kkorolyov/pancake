package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.core.component.media.Graphic;
import dev.kkorolyov.pancake.platform.Config;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.entity.Signature;
import dev.kkorolyov.pancake.platform.event.CameraCreated;
import dev.kkorolyov.pancake.platform.event.CanvasCreated;
import dev.kkorolyov.pancake.platform.math.Vector;
import dev.kkorolyov.pancake.platform.media.Camera;
import dev.kkorolyov.pancake.platform.utility.Limiter;
import dev.kkorolyov.pancake.platform.utility.PerformanceCounter.Usage;
import dev.kkorolyov.simplelogs.Logger;

import javafx.application.Platform;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
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

	private Canvas canvas;
	private GraphicsContext g;

	private Camera camera;
	private final Rotate rotate = new Rotate();
	private final Vector rotateVector = new Vector();

	private final NavigableMap<Float, Set<Entity>> drawBuckets = new TreeMap<>();

	/**
	 * Constructs a new render system.
	 */
	public RenderSystem() {
		super(
				new Signature(Transform.class, Graphic.class),
				new Limiter(0)
		);
	}
	@Override
	public void attach() {
		resources.events
				.register(CanvasCreated.class, e -> {
					canvas = e.getCanvas();
					g = canvas.getGraphicsContext2D();
				})
				.register(CameraCreated.class, e -> camera = e.getCamera());
	}

	@Override
	public void before(long dt) {
		for (Collection<Entity> bucket : drawBuckets.values()) bucket.clear();
	}

	@Override
	public void update(Entity entity, long dt) {
		drawBuckets.computeIfAbsent(entity.get(Transform.class).getPosition().getZ(), k -> new HashSet<>())
				.add(entity);
	}

	@Override
	public void after(long dt) {
		Collection<Entity> toDraw = drawBuckets.values().stream()
				.flatMap(Collection::stream)
				.collect(toList());

		Platform.runLater(() -> {
			g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

			for (Entity entity : toDraw) {
				draw(
						entity.get(Transform.class),
						entity.get(Graphic.class)
				);
			}
			rotate(0, null);

			drawDebug();
		});
	}

	private void draw(Transform transform, Graphic graphic) {
		Vector drawPosition = camera.getRelativePosition(transform.getGlobalPosition());

		rotateVector.set(transform.getGlobalOrientation());
		rotateVector.add(graphic.getOrientationOffset());

		rotate((float) -Math.toDegrees(rotateVector.getZ()), drawPosition);  // Rotate around transform origin
		drawPosition.sub(graphic.size(), .5f);  // Sprite top-left corner

		graphic.render(g, drawPosition);
	}

	private void drawDebug() {
		String[] args = Config.config.getArray("renderInfo");
		if (args == null) return;

		double y = 0;

		for (String arg : args) {
			switch (arg) {
				case "fps":
					g.strokeText("FPS: " + resources.performanceCounter.getTps(), 0, y += LINE_HEIGHT);
					break;
				case "usage":
					for (Usage usage : resources.performanceCounter.getUsages()) {
						Paint previous = g.getStroke();

						if (usage.exceedsMax()) g.setStroke(Color.DARKRED);
						g.strokeText(usage.toString(), 0, y += LINE_HEIGHT);
						g.setStroke(previous);
					}
					break;
				case "id":
					TextAlignment previousAlign = g.getTextAlign();
					VPos previousBaseline = g.getTextBaseline();

					g.setTextAlign(TextAlignment.CENTER);
					g.setTextBaseline(VPos.CENTER);

					for (Collection<Entity> bucket : drawBuckets.values()) {
						for (Entity entity : bucket) {
							Vector drawPosition = camera.getRelativePosition(
									entity.get(Transform.class).getGlobalPosition()
							).translate(0, entity.get(Graphic.class).size().getY() * .5f);

							g.strokeText(String.valueOf(entity.getId()), drawPosition.getX(), drawPosition.getY());
						}
					}
					g.setTextAlign(previousAlign);
					g.setTextBaseline(previousBaseline);
					break;
				default:
					log.warning("Unknown renderInfo arg: {}", arg);
			}
		}
	}

	private void rotate(float angle, Vector pivot) {
		rotate.setAngle(angle);

		if (pivot != null) {
			rotate.setPivotX(pivot.getX());
			rotate.setPivotY(pivot.getY());
		}
		g.setTransform(
				rotate.getMxx(), rotate.getMyx(),
				rotate.getMxy(), rotate.getMyy(),
				rotate.getTx(), rotate.getTy()
		);
	}
}
