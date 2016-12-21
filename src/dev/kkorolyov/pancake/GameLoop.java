package dev.kkorolyov.pancake;

import javafx.animation.AnimationTimer;

/**
 * Main game loop.
 */
public class GameLoop extends AnimationTimer {
	private Renderer renderer;
	
	/**
	 * Constructs a new game loop.
	 * @param renderer renderer used for drawing
	 */
	public GameLoop(Renderer renderer) {
		this.renderer = renderer;
	}
	
	@Override
	public void handle(long now) {
		pollKeys();	// Adds event for each pressed key
		tick();
		render();
	}
}
