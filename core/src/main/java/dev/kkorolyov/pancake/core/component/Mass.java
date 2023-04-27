package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.entity.Component;

/**
 * Mass of an entity.
 */
public final class Mass implements Component {
	private double value;

	/**
	 * Constructs a new mass.
	 * @param value initial mass in {@code kg}
	 */
	public Mass(double value) {
		this.value = value;
	}

	/** @return mass in {@code kg} */
	public double getValue() {
		return value;
	}
	/** @param value mass in {@code kg} */
	public void setValue(double value) {
		this.value = value;
	}
}
