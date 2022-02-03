package dev.kkorolyov.pancake.platform;

/**
 * Main game loop.
 */
public final class GameLoop {
	private final GameEngine engine;
	private final long rate;

	private volatile long effectiveRate;
	private volatile boolean active;

	/**
	 * Constructs a new game loop for {@code engine} with update rate specified in platform {@link Config#get()} as {@code tps} - target number of ticks per second.
	 */
	public GameLoop(GameEngine engine) {
		this(engine, (long) (1e9 / Integer.parseInt(Config.get().getProperty("tps"))));
	}
	/**
	 * Constructs a new game loop updating {@code engine} every {@code rate} nanoseconds.
	 */
	public GameLoop(GameEngine engine, long rate) {
		this.engine = engine;
		this.rate = rate;
		effectiveRate = rate;
	}

	/**
	 * Starts this loop on a new thread if it is not currently active.
	 * An active game loop continuously updates its associated {@link GameEngine} by {@code rate * scale} increments every {@code >= rate} passage of time.
	 */
	public void start() {
		if (!active) {
			active = true;

			new Thread(() -> {
				long last = System.nanoTime();
				long lag = 0L;

				while (active) {
					long now = System.nanoTime();
					long elapsed = now - last;
					last = now;
					lag += elapsed;

					while (lag >= rate) {
						engine.update(effectiveRate);
						lag -= rate;
					}
				}
			}).start();
		}
	}
	/**
	 * Halts execution of this loop if it is currently active.
	 */
	public void stop() {
		active = false;
	}

	/**
	 * Returns the game engine run by this loop.
	 */
	public GameEngine getEngine() {
		return engine;
	}

	/**
	 * Returns the minimum time in {@code ns} that must elapse between this loop's updates.
	 */
	public long getRate() {
		return rate;
	}

	/**
	 * Returns the scale factor of loop time to game engine update time.
	 */
	public double getScale() {
		return (double) effectiveRate / rate;
	}
	/**
	 * Sets the loop time to engine update time factor to {@code scale}.
	 * e.g. {@code 1} (default) updates the game engine with the same rate value that this loop updates, whereas {@code 2} updates the game engine with double the rate value.
	 */
	public void setScale(double scale) {
		effectiveRate = (long) (rate * scale);
	}

	/**
	 * Returns whether this loop is currently running.
	 */
	public boolean isActive() {
		return active;
	}

	@Override
	public String toString() {
		return "GameLoop{" +
				"engine=" + engine +
				", rate=" + rate +
				", effectiveRate=" + effectiveRate +
				", active=" + active +
				'}';
	}
}
