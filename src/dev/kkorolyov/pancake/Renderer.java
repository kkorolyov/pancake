package dev.kkorolyov.pancake;

import javafx.scene.canvas.Canvas;

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
	public void render() {
		// TODO Pass entities
	}
}
