package dev.kkorolyov.pancake.system;

import dev.kkorolyov.pancake.Config;
import dev.kkorolyov.pancake.GameSystem;
import dev.kkorolyov.pancake.component.Sprite;
import dev.kkorolyov.pancake.component.Transform;
import dev.kkorolyov.pancake.entity.Entity;
import dev.kkorolyov.pancake.entity.Signature;
import dev.kkorolyov.pancake.graphics.Camera;
import dev.kkorolyov.pancake.math.Vector;

import java.util.HashSet;
import java.util.Set;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

/**
 * Renders all game entities.
 */
public class RenderSystem extends GameSystem {
	private final Canvas canvas;
	private final GraphicsContext g;

	private final Camera camera;
	private final Rotate rotate = new Rotate();

	private final Set<Sprite> tickedSprites = new HashSet<>();

	/**
	 * Constructs a new render system.
	 * @param canvas canvas on which to render
	 * @param camera view of render space
	 */
	public RenderSystem(Canvas canvas, Camera camera) {
		super(new Signature(Transform.class,
												Sprite.class),
					(e1, e2) -> e1.get(Transform.class).getPosition().getZ() < e2.get(Transform.class).getPosition().getZ() ? -1 : 1);

		this.canvas = canvas;
		g = canvas.getGraphicsContext2D();

		this.camera = camera;
	}

	@Override
	public void before(float dt) {
		tickedSprites.clear();

		g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	@Override
	public void update(Entity entity, float dt) {
		Transform transform = entity.get(Transform.class);
		Sprite sprite = entity.get(Sprite.class);

		if (!tickedSprites.contains(sprite)) {
			sprite.update(dt);
			tickedSprites.add(sprite);
		}
		draw(transform, sprite);
	}

	@Override
	public void after(float dt) {
		rotate(0, null);

		drawDebug(dt);
	}

	private void draw(Transform transform, Sprite sprite) {
		Vector drawPosition = camera.getRelativePosition(transform.getPosition());

		rotate(transform.getRotation(), drawPosition);	// Rotate around transform origin
		drawPosition.sub(sprite.getSize(), .5f);	// Sprite top-left corner

		for (Image image : sprite.getImage()) {
			g.drawImage(image, sprite.getOrigin().getX(), sprite.getOrigin().getY(), sprite.getSize().getX(), sprite.getSize().getY(),
									drawPosition.getX(), drawPosition.getY(), sprite.getSize().getX(), sprite.getSize().getY());
		}
	}

	private void drawDebug(float dt) {
		String[] args = Config.config.getArray("renderInfo");
		if (args == null) return;

		for (String arg : args) {
			switch (arg) {
				case "FPS":
					g.strokeText("FPS: " + Math.round(1 / dt), 0, 10);
					break;
			}
		}
	}

	private void rotate(float angle, Vector pivot) {
		rotate.setAngle(angle);

		if (pivot != null) {
			rotate.setPivotX(pivot.getX());
			rotate.setPivotY(pivot.getY());
		}
		g.setTransform(rotate.getMxx(), rotate.getMyx(),
									 rotate.getMxy(), rotate.getMyy(),
									 rotate.getTx(), rotate.getTy());
	}
}
