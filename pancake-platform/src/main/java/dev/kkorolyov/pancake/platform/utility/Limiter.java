package dev.kkorolyov.pancake.platform.utility;

/**
 * A timer which is ready after a set number of {@code ns} have elapsed since its previous ready state.
 */
public class Limiter {
	private long frequency;
	private long elapsed;

	/**
	 * Constructs a new limiter.
	 * @param frequency minimum {@code ns} that must elapse between ready states
	 */
	public Limiter(long frequency) {
		this.frequency = Math.max(0, frequency);
	}

	/**
	 * Adds elapsed time to this limiter and checks its ready state.
	 * @param dt additional elapsed {@code ns}
	 * @return {@code true} if {@code >= frequency ns} have elapsed since the last time this was ready
	 */
	public boolean isReady(float dt) {
		elapsed += dt;
		return isReady();
	}
	/** @return {@code true} if {@code >= frequency ns} have elapsed since the last time this was ready */
	public boolean isReady() {
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
