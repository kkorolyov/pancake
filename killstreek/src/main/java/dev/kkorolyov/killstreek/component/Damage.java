package dev.kkorolyov.killstreek.component;

import dev.kkorolyov.pancake.platform.entity.Component;

/**
 * Applies damage values to an entity.
 */
public class Damage implements Component {
	private int value;
	private double effectiveValue;
	private int remaining;

	private long duration;
	private long elapsed;

	/**
	 * Constructs a new, expired damage.
	 */
	public Damage() {
		this(0, 0);
	}
	/**
	 * Constructs a new damage which applies its entire value at once.
	 * @param value damage value
	 */
	public Damage(int value) {
		setValue(value);
	}
	/**
	 * Constructs a new damage.
	 * @param value damage value
	 * @param duration {@code ns} over which to distribute applied damage, values close to (but not at) {@code 0} result in effectively immediate damage application
	 */
	public Damage(int value, long duration) {
		setValue(value, duration);
	}

	/**
	 * Applies the negation of this damage's value to some health.
	 * If this damage has a duration, the appropriate portion of its value is applied instead.
	 * If this damage has expired, this method does nothing.
	 * @param health damaged health
	 * @param dt {@code ns} elapsed since previous invocation of this method
	 * @return {@code false} if expired, else {@code true}
	 */
	public boolean apply(Health health, long dt) {
		if (isExpired()) return false;
		elapsed += dt;

		effectiveValue += (isExpired()) ? remaining : value * (Math.min((double) dt / duration, 1));
		if (Double.compare(Math.abs(effectiveValue), 1) >= 0) {
			int removed = (int) -effectiveValue;

			remaining += removed;

			health.change(removed);
			effectiveValue = 0;
		}
		return true;
	}

	/**
	 * Resets elapsed damage duration.
	 */
	public void reset() {
		setValue(value, duration);
	}

	/** @return {@code true} if subsequent damage applications will do nothing */
	public boolean isExpired() {
		return elapsed >= duration;
	}

	/** @return damage value */
	public int getValue() {
		return value;
	}
	/**
	 * @param value new damage value
	 * @return {@code this}
	 */
	public Damage setValue(int value) {
		return setValue(value, 1);
	}
	/**
	 * @param value new damage value
	 * @param duration seconds over which to distribute applied damage, values close to (but not at) {@code 0} result in effectively immediate damage application
	 * @return {@code this}
	 */
	public Damage setValue(int value, long duration) {
		this.value = value;
		effectiveValue = 0;
		remaining = value;

		this.duration = duration;
		elapsed = Math.min(0, duration);

		return this;
	}

	/** @return seconds over which damage distributed */
	public float getDuration() {
		return duration;
	}
}
