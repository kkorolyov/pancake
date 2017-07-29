package dev.kkorolyov.killstreek.component;

import dev.kkorolyov.pancake.entity.Component;

/**
 * Maintains an entity's state of existence.
 */
public class Health implements Component {
	private int max;
	private int current;

	/**
	 * Constructs a new health with initial health set to {@code max}.
	 * @param max maximum value for current health, constrained {@code > 0}
	 */
	public Health(int max) {
		this(max, max);
	}
	/**
	 * Constructs a new health.
	 * @param max maximum value for current health, constrained {@code > 0}
	 * @param current initial health, constrained {@code [0, max]}
	 */
	public Health(int max, int current) {
		setMax(max);
		setCurrent(current);
	}

	/**
	 * Applies damage to current health.
	 * @param damage current health decrement amount, negative values increment
	 */
	public void accept(int damage) {
		current -= damage;
	}

	/** @return {@code true} if current health {@code <= 0} */
	public boolean isDead() {
		return current <= 0;
	}

	/** @return maximum health */
	public int getMax() {
		return max;
	}
	/**
	 * Constrained {@code > 0}.
	 * @param max new maximum health
	 * @return {@code this}
	 */
	public Health setMax(int max) {
		this.max = Math.max(1, max);
		return this;
	}

	/** @return current health */
	public int getCurrent() {
		return current;
	}
	/**
	 * Constrained {@code [0, max]}.
	 * @param current new current health
	 * @return {@code this}
	 */
	public Health setCurrent(int current) {
		this.current = Math.max(0, Math.min(current, max));
		return this;
	}
}
