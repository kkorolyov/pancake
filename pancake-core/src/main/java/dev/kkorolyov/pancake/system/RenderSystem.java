package dev.kkorolyov.pancake.system;

import java.util.HashSet;
import java.util.Set;

import dev.kkorolyov.pancake.Entity;
import dev.kkorolyov.pancake.GameSystem;
import dev.kkorolyov.pancake.Signature;
import dev.kkorolyov.pancake.component.Sprite;
import dev.kkorolyov.pancake.component.Transform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * Renders all game entities.
 */
public class RenderSystem extends GameSystem {
	private final Canvas canvas;
	private final GraphicsContext g;
	private final Set<Sprite> tickedSprites = new HashSet<>();

	/**
	 * Constructs a new render system.
	 * @param canvas canvas on which to render
	 */
	public RenderSystem(Canvas canvas) {
		super(new Signature(Transform.class,
												Sprite.class));

		this.canvas = canvas;

		g = canvas.getGraphicsContext2D();
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
			sprite.tick(dt);
			tickedSprites.add(sprite);
		}
		sprite.draw(g, transform.getPosition());
	}

	@Override
	public void after(float dt) {
		g.strokeText("FPS: " + Math.round(1 / dt), 0, 10);
	}
}
