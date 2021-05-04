package dev.kkorolyov.pancake.platform;

/**
 * Main game loop.
 */
public final class GameLoop {
	private final GameEngine engine;
	private final long dt;

	private volatile boolean active;

	/**
	 * Constructs a new game loop with tick granularity specified in platform {@link Config#get()}.
	 * @param engine game engine receiving updates
	 */
	public GameLoop(GameEngine engine) {
		this(engine, (long) (1e9 / Integer.parseInt(Config.get().getProperty("tps"))));
	}
	/**
	 * Constructs a new game loop.
	 * @param engine game engine receiving updates
	 * @param dt tick granularity in {@code ns}
	 */
	public GameLoop(GameEngine engine, long dt) {
		this.engine = engine;
		this.dt = dt;
	}

	/**
	 * Starts this loop if it is not currently active.
	 * An active game loop continuously updates its associated {@link GameEngine} by {@code dt} increments every {@code >= dt} passage of time.
	 */
	public void start() {
		if (!active) {
			active = true;
			long last = System.nanoTime();
			long lag = 0L;

			while (active) {
				long now = System.nanoTime();
				long elapsed = now - last;
				last = now;
				lag += elapsed;

				while (lag >= dt) {
					engine.update(dt);
					lag -= dt;
				}
			}
		}
	}
	/**
	 * Halts execution of this loop if it is currently active.
	 */
	public void stop() {
		active = false;
	}

	@Override
	public String toString() {
		return "GameLoop{" +
				"engine=" + engine +
				", dt=" + dt +
				", active=" + active +
				'}';
	}
}
