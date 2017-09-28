package dev.kkorolyov.killstreek.component;

import dev.kkorolyov.killstreek.utility.BoundedValue;
import dev.kkorolyov.pancake.platform.entity.Component;

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
	/** @return {@code true} if current health {@code < 0} */
	public boolean isSuperDead() {
		return value.get() < 0;
	}

	/** @return health value */
	public BoundedValue<Integer> getValue() {
		return value;
	}
}
