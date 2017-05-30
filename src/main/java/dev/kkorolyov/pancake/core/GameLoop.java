package dev.kkorolyov.pancake.core;

import javafx.animation.AnimationTimer;

/**
 * Main game loop.
 */
public class GameLoop extends AnimationTimer {
	private static final float ELAPSED_TO_S = 1 / 1000000000f;

	private final GameEngine engine;
	private long last;
	private long total;

	/**
	 * Constructs a new game loop.
	 * @param engine game engine receiving updates
	 */
	public GameLoop(GameEngine engine) {
		this.engine = engine;
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
