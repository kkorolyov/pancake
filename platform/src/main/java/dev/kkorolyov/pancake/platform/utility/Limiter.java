package dev.kkorolyov.pancake.platform.utility;

import dev.kkorolyov.pancake.platform.Config;
import dev.kkorolyov.pancake.platform.GameSystem;

import java.util.Optional;

/**
 * A timer which is ready after a set number of {@code ns} have elapsed since its previous ready state.
 */
public final class Limiter {
	private long frequency;
	private long elapsed;

	/**
	 * Constructs a new limiter.
	 * @param frequency minimum {@code ns} that must elapse between ready states
	 */
	public Limiter(long frequency) {
		this.frequency = ArgVerify.greaterThanEqual("frequency", 0, frequency);
	}

	/**
	 * @param c game system to get limiter configuration for
	 * @return limiter using TPS (ticks per second) specified in {@code c}'s configuration; or no limit if no configuration
	 */
	public static Limiter fromConfig(Class<? extends GameSystem> c) {
		return new Limiter(
				Optional.ofNullable(Config.get(c).getProperty("tps"))
						.map(Integer::parseInt)
						.map(tps -> (long) (1e9 / tps))
						.orElse(0L)
		);
	}

	/**
	 * Adds elapsed time to this limiter and checks its ready state.
	 * @param dt additional elapsed {@code ns}
	 * @return {@code true} if {@code >= frequency ns} have elapsed since the last time this was ready
	 */
	public boolean isReady(long dt) {
		elapsed += dt;
		return elapsed >= frequency;
	}

	/**
	 * Returns elapsed {@code ns} since last consumption of this limiter and resets {@code elapsed}.
	 * @return elapsed {@code ns} since last consumption
	 */
	public long consumeElapsed() {
		long result = elapsed;
		elapsed = 0;
		return result;
	}
}
