package dev.kkorolyov.pancake.platform.utility;

/**
 * A timer which is ready after a set number of seconds have passed.
 */
public class Limiter {
	private float duration;
	private float elapsed;

	/**
	 * Constructs a new limiter.
	 * @param duration seconds between which when this limiter is ready
	 */
	public Limiter(float duration) {
		this.duration = duration;
	}

	/**
	 * Adds elapsed time to this limiter and checks its ready state.
	 * @param dt additional elapsed seconds
	 * @return {@code true} if {@code >= duration} seconds have passed since the last time this was ready
	 */
	public boolean isReady(float dt) {
		elapsed += dt;
		boolean isReady = isReady();

		if (isReady) elapsed = 0;
		return isReady;
	}
	/** @return {@code true} if {@code >= duration} seconds have passed since the last time this was ready */
	public boolean isReady() {
		return elapsed >= duration;
	}

	/** @return limiter duration in seconds */
	public float getDuration() {
		return duration;
	}
	/**
	 * Sets a new limiter duration and resets elapsed time.
	 * @param duration new seconds between which this limiter is ready
	 */
	public void setDuration(float duration) {
		this.duration = duration;
		elapsed = Math.min(0, duration);
	}
}
