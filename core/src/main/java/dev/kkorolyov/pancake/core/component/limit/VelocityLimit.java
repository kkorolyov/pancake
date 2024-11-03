package dev.kkorolyov.pancake.core.component.limit;

import dev.kkorolyov.pancake.core.component.Velocity;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.utility.ArgVerify;

/**
 * Constrains the maximum attainable speed of an entity.
 */
public final class VelocityLimit implements Limit<Velocity> {
	// TODO split min and max
	private double linear;
	private double angular;

	/**
	 * Constructs a new velocity magnitude limit of {@code linear} {@code m/s} and {@code angular} {@code rad/s}.
	 */
	public VelocityLimit(double linear, double angular) {
		setLinear(linear);
		setAngular(angular);
	}

	/**
	 * Returns current linear limit in {@code m/s}.
	 */
	public double getLinear() {
		return linear;
	}
	/**
	 * Sets linear limit to {@code linear} {@code m/s}.
	 */
	public void setLinear(double linear) {
		this.linear = ArgVerify.greaterThanEqual("linear", 0.0, linear);
	}

	/**
	 * Returns current angular limit in {@code rad/s}
	 */
	public double getAngular() {
		return angular;
	}
	/**
	 * Sets angular limit to {@code angular} {@code rad/s}.
	 */
	public void setAngular(double angular) {
		this.angular = ArgVerify.greaterThanEqual("angular", 0.0, angular);
	}

	@Override
	public void limit(Velocity component) {
		double currentLinear = Vector3.magnitude(component.getLinear());
		if (currentLinear > linear) component.getLinear().scale(linear / currentLinear);

		double currentAngular = Vector3.magnitude(component.getAngular());
		if (currentAngular > angular) component.getAngular().scale(angular / currentAngular);
	}
}
