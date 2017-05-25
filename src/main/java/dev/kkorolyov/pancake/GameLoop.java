package dev.kkorolyov.pancake;

import dev.kkorolyov.pancake.system.RenderSystem;
import javafx.animation.AnimationTimer;

/**
 * Main game loop.
 */
public class GameLoop extends AnimationTimer {
	private static final float ELAPSED_TO_S = 1 / 1000000000;
	private long 	last,
								total;
	private RenderSystem renderer;
	
	/**
	 * Constructs a new game loop.
	 * @param renderer renderer used for drawing
	 */
	public GameLoop(RenderSystem renderer) {
		this.renderer = renderer;
	}
	
	@Override
	public void handle(long now) {
		if (last == 0)
			last = now;
		long elapsed = now - last;
		float dt = elapsed * ELAPSED_TO_S;
		
		tick();
		render();
		
		total += elapsed;
		last = now;
	}
}
