package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.utility.ArgVerify;

/**
 * Damping applied on an entity.
 * Damping values are in the interval {@code [0, 1]}, essentially translating to {@code [immediate stop, no damping]}.
 */
public final class Damping implements Component {
	private static final double EFFECTIVE_ZERO = 1e-9;
	private final Vector3 linear;
	private final Vector3 angular;

	/**
	 * Constructs a new damping from {@code linear} and {@code angular} damping values.
	 */
	public Damping(Vector3 linear, Vector3 angular) {
		this.linear = Vector3.of(verify(linear.getX()), verify(linear.getY()), verify(linear.getZ()));
		this.angular = Vector3.of(verify(angular.getX()), verify(angular.getY()), verify(angular.getZ()));
	}
	private static double verify(double value) {
		return ArgVerify.betweenInclusive("damping", 0.0, 1.0, value);
	}

	/**
	 * Damps linear and angular velocity vectors along axes where applied force/torque is zero or in the opposite direction of velocity.
	 * Damping is done by multiplying a velocity component by the respective damping component, so, the smaller the damping value, the greater the decrease in velocity.
	 */
	public void damp(Vector3 velocityLinear, Vector3 velocityAngular, Vector3 force, Vector3 torque) {
		velocityLinear.setX(velocityLinear.getX() * damp(velocityLinear.getX(), force.getX(), linear.getX()));
		velocityLinear.setY(velocityLinear.getY() * damp(velocityLinear.getY(), force.getY(), linear.getY()));
		velocityLinear.setZ(velocityLinear.getZ() * damp(velocityLinear.getZ(), force.getZ(), linear.getZ()));

		velocityAngular.setX(velocityAngular.getX() * damp(velocityAngular.getX(), torque.getX(), angular.getX()));
		velocityAngular.setY(velocityAngular.getY() * damp(velocityAngular.getY(), torque.getY(), angular.getY()));
		velocityAngular.setZ(velocityAngular.getZ() * damp(velocityAngular.getZ(), torque.getZ(), angular.getZ()));
	}
	private static double damp(double velocity, double force, double damping) {
		return (velocity < 0 ? force >= -EFFECTIVE_ZERO : force <= EFFECTIVE_ZERO) ? damping : 1;
	}

	/**
	 * Returns the linear velocity damping vector.
	 */
	public Vector3 getLinear() {
		return linear;
	}
	/**
	 * Returns the angular velocity damping vector.
	 */
	public Vector3 getAngular() {
		return angular;
	}
}
