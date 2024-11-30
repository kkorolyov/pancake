package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.entity.Component;

import java.util.Objects;

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

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Mass other)) return false;
		return Double.compare(value, other.value) == 0;
	}
	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}
}
