package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.math.Matrix4;
import dev.kkorolyov.pancake.platform.math.Vector3;

import java.util.Objects;

/**
 * Velocity of a moving entity.
 * Maintains a linear velocity in {@code m/s} and an angular velocity in {@code rad/s}.
 */
public final class Velocity implements Component {
	private static final Vector3 xAxis = Vector3.of(1);
	private static final Vector3 yAxis = Vector3.of(0, 1);
	private static final Vector3 zAxis = Vector3.of(0, 0, 1);

	private final Vector3 linear = Vector3.of();
	private final Vector3 angular = Vector3.of();

	/**
	 * Applies a positional change to {@code position} given the current linear velocity and {@code seconds} interval.
	 */
	public void move(Vector3 position, double seconds) {
		position.add(linear, seconds);
	}
	/**
	 * Applies a rotational change to {@code rotation} given the current angular velocity and {@code seconds} interval.
	 */
	public void rotate(Matrix4 rotation, double seconds) {
		rotation.rotate(angular.getX() * seconds, xAxis);
		rotation.rotate(angular.getY() * seconds, yAxis);
		rotation.rotate(angular.getZ() * seconds, zAxis);
	}

	/**
	 * Returns the linear velocity in {@code m/s}.
	 */
	public Vector3 getLinear() {
		return linear;
	}
	/**
	 * Returns the angular velocity in {@code rad/s}.
	 */
	public Vector3 getAngular() {
		return angular;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Velocity other)) return false;
		return Objects.equals(linear, other.linear) && Objects.equals(angular, other.angular);
	}
	@Override
	public int hashCode() {
		return Objects.hash(linear, angular);
	}
}
