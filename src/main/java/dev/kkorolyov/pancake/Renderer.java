package dev.kkorolyov.pancake;

import dev.kkorolyov.pancake.entity.Entity;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Renders all game entities.
 */
public class Renderer {
	private final Canvas canvas;
	
	/**
	 * Constructs a new renderer.
	 * @param canvas canvas on which to render
	 */
	public Renderer(Canvas canvas) {
		this.canvas = canvas;
	}
	
	/**
	 * Renders entities on the canvas.
	 */
	public void render(Iterable<Entity> entities) {	// TODO Temp
		GraphicsContext g = canvas.getGraphicsContext2D();
		
		g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		for (Entity entity : entities) {
			double 	x = entity.getBounds().getOrigin().getX(),
							y = entity.getBounds().getOrigin().getY();
			Image image = entity.getSprite().getImage();
			
			g.drawImage(image, x, y);
		}
	}
}
