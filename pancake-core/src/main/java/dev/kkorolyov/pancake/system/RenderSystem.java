package dev.kkorolyov.pancake.system;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import dev.kkorolyov.pancake.Config;
import dev.kkorolyov.pancake.GameSystem;
import dev.kkorolyov.pancake.component.Chain;
import dev.kkorolyov.pancake.component.Sprite;
import dev.kkorolyov.pancake.component.Transform;
import dev.kkorolyov.pancake.entity.Entity;
import dev.kkorolyov.pancake.entity.Signature;
import dev.kkorolyov.pancake.math.Vector;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * Renders all game entities.
 */
public class RenderSystem extends GameSystem {
	private static final Signature cameraSignature = new Signature(Chain.class);

	private final Canvas canvas;
	private final GraphicsContext g;

	private final Vector unitPixels;
	private final Vector camera;
	private final Vector drawPosition = new Vector();

	private final Set<Sprite> tickedSprites = new HashSet<>();
	private final Map<Transform, Sprite> drawQueue = new TreeMap<>((t1, t2) -> t1.getPosition().getZ() < t2.getPosition().getZ() ? -1 : 1);

	/**
	 * Constructs a new render system.
	 * @param canvas canvas on which to render
	 * @param unitPixels number of pixels within 1 unit distance
	 * @param camera viewport center
	 */
	public RenderSystem(Canvas canvas, float unitPixels, Vector camera) {
		super(new Signature(Transform.class,
												Sprite.class));

		this.canvas = canvas;
		g = canvas.getGraphicsContext2D();

		this.unitPixels = new Vector(unitPixels, -unitPixels);	// Render as +y == up
		this.camera = camera;
	}

	@Override
	public void before(float dt) {
		tickedSprites.clear();
		drawQueue.clear();

		g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	@Override
	public void update(Entity entity, float dt) {
		Transform transform = entity.get(Transform.class);
		Sprite sprite = entity.get(Sprite.class);

		drawQueue.put(transform, sprite);

		if (!tickedSprites.contains(sprite)) {
			sprite.tick(dt);
			tickedSprites.add(sprite);
		}
	}

	@Override
	public void after(float dt) {
		for (Entry<Transform, Sprite> entry: drawQueue.entrySet()) {
			draw(entry.getKey(), entry.getValue());
		}
		drawDebug(Config.config.getArray("renderInfo"), dt);
	}

	private void draw(Transform transform, Sprite sprite) {
		drawPosition.set(transform.getPosition());	// Raw position
		drawPosition.sub(camera); // Position relative to camera

		drawPosition.scale(unitPixels);	// Scale to pixels

		drawPosition.translate((float) canvas.getWidth() / 2, (float) canvas.getHeight() / 2);	// Position relative to display center
		drawPosition.sub(sprite.getSize(), .5f);	// Sprite top-left corner

		g.drawImage(sprite.getBaseImage(), sprite.getOrigin().getX(), sprite.getOrigin().getY(), sprite.getSize().getX(), sprite.getSize().getY(),
								drawPosition.getX(), drawPosition.getY(), sprite.getSize().getX(), sprite.getSize().getY());
	}
	private void drawDebug(String[] args, float dt) {
		if (args == null) return;

		for (String arg : args) {
			switch (arg) {
				case "FPS":
					g.strokeText("FPS: " + Math.round(1 / dt), 0, 10);
					break;
			}
		}
	}
}
