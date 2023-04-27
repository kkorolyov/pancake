package dev.kkorolyov.pancake.core.component.limit;

import dev.kkorolyov.pancake.core.component.Velocity;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.utility.ArgVerify;

/**
 * Constrains the maximum attainable speed of an entity.
 */
public final class VelocityLimit implements Limit<Velocity> {
	private double value;

	/**
	 * Constructs a new velocity magnitude limit of {@code value} {@code m/s}.
	 */
	public VelocityLimit(double value) {
		setValue(value);
	}

	/**
	 * Returns current limit in {@code m/s}.
	 */
	public double getValue() {
		return value;
	}
	/**
	 * Sets limit to {@code value} {@code m/s}.
	 */
	public void setValue(double value) {
		this.value = ArgVerify.greaterThanEqual("value", 0.0, value);
	}

	@Override
	public void limit(Velocity component) {
		double current = Vector3.magnitude(component.getValue());
		if (current > value) component.getValue().scale(value / current);
	}
}
