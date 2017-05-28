package dev.kkorolyov.pancake.system;

import java.util.Map.Entry;

import dev.kkorolyov.pancake.engine.Component;
import dev.kkorolyov.pancake.engine.EntityManager;
import dev.kkorolyov.pancake.engine.GameSystem;
import dev.kkorolyov.pancake.component.Sprite;
import dev.kkorolyov.pancake.component.Transform;
import dev.kkorolyov.pancake.math.Vector;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * Renders all game entities.
 */
public class RenderSystem extends GameSystem {
	private final Canvas canvas;
	
	/**
	 * Constructs a new render system.
	 * @param canvas canvas on which to render
	 */
	public RenderSystem(Canvas canvas) {
		this.canvas = canvas;
	}
	
	@Override
	public int update(float dt) {
		GraphicsContext g = canvas.getGraphicsContext2D();
		g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		int counter = 0;
		EntityManager entities = getEntities();
		for (Entry<Integer, Component> entity : entities.getAll(Transform.class)) {
			Transform transform = (Transform) entity.getValue();
			Sprite sprite = entities.get(entity.getKey(), Sprite.class);
			
			if (transform != null && sprite != null) {
				Vector position = transform.getPosition();
				g.drawImage(sprite.getImage(), position.getX(), position.getY());
				
				counter++;
			}
		}
		g.strokeText("FPS: " + Math.round(1 / dt), 0, 10);
		
		return counter;
	}
}
