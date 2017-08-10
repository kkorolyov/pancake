package dev.kkorolyov.killstreek.component;

import dev.kkorolyov.killstreek.utility.BoundedValue;
import dev.kkorolyov.pancake.entity.Component;

/**
 * Maintains an entity's state of existence.
 */
public class Health implements Component {
	private BoundedValue<Integer> value;

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
	 * @param current initial health, constrained {@code <= max}
	 */
	public Health(int max, int current) {
		value = new BoundedValue<>(null, max, current);
		setMax(max);
		setCurrent(current);
	}

	/**
	 * Applies a change to current health.
	 * @param amount change in current health
	 */
	public void change(int amount) {
		value.set(value.get() + amount);
	}

	/** @return {@code true} if current health {@code <= 0} */
	public boolean isDead() {
		return value.get() <= 0;
	}

	/** @return maximum health */
	public int getMax() {
		return value.getMaximum();
	}
	/**
	 * Constrained {@code > 0}.
	 * @param max new maximum health
	 * @return {@code this}
	 */
	public Health setMax(int max) {
		value.setMaximum(Math.max(1, max));
		return this;
	}

	/** @return current health */
	public int getCurrent() {
		return value.get();
	}
	/**
	 * Constrained {@code <= max}.
	 * @param current new current health
	 * @return {@code this}
	 */
	public Health setCurrent(int current) {
		value.set(current);
		return this;
	}
}
