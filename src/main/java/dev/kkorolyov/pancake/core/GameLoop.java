package dev.kkorolyov.pancake.core;

import dev.kkorolyov.pancake.system.RenderSystem;
import javafx.animation.AnimationTimer;

/**
 * Main game loop.
 */
public class GameLoop extends AnimationTimer {
	private static final float ELAPSED_TO_S = 1 / 1000000000;

	private final GameEngine engine = new GameEngine();
	private long last;
	private long total;

	/**
	 * Constructs a new game loop using a new {@link GameEngine} and rendering using {@code renderer}.
	 * @param renderer rendering system
	 */
	public GameLoop(RenderSystem renderer) {
		engine.add(renderer);
	}
	
	@Override
	public void handle(long now) {
		if (last == 0) last = now;
		long elapsed = now - last;
		float dt = elapsed * ELAPSED_TO_S;
		
		engine.update(dt);
		
		total += elapsed;
		last = now;
	}
}
