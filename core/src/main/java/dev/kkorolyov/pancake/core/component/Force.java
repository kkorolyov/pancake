package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.math.FloatOps;
import dev.kkorolyov.pancake.platform.math.Vector3;

/**
 * Net force acting on an entity.
 * Maintains a value of {@code N} and offset from the center of an affected point mass in {@code m}.
 */
public final class Force implements Component {
	private static final Vector3 zeroV = Vector3.of();

	private final Vector3 value = Vector3.of();
	private final Vector3 offset = Vector3.of();

	/**
	 * Applies to {@code linear} and {@code angular} velocities, accelerations calculated from net force value, {@code mass}, and {@code seconds} interval.
	 */
	public void accelerate(Vector3 linear, Vector3 angular, double mass, double seconds) {
		if (!value.equals(zeroV)) {
			// linear change
			linear.add(value, seconds / mass);  // a = f/m, v' = v + at

			// angular change
			if (!offset.equals(zeroV)) {
				// scale of projection of force along offset - used to calculate remaining perpendicular force
				var projScale = Vector3.dot(value, offset) / Vector3.dot(offset, offset);
				// scale by moment arm, moment of inertia, and time
				var momentTimeScale = Vector3.magnitude(offset) * seconds / mass;
				var xForce = (value.getX() - offset.getX() * projScale) * momentTimeScale;
				var yForce = (value.getY() - offset.getY() * projScale) * momentTimeScale;
				var zForce = (value.getZ() - offset.getZ() * projScale) * momentTimeScale;

				angular.setX(angular.getX() + getAxisAcceleration(yForce, zForce, offset.getY(), offset.getZ()));
				angular.setY(angular.getY() + getAxisAcceleration(xForce, zForce, offset.getX(), offset.getZ()));
				angular.setZ(angular.getZ() + getAxisAcceleration(xForce, yForce, offset.getX(), offset.getY()));
			}
		}
	}
	private double getAxisAcceleration(double aForce, double bForce, double aOffset, double bOffset) {
		double magnitude = !(FloatOps.equals(aForce, 0) || FloatOps.equals(bOffset, 0)) || !(FloatOps.equals(bForce, 0) || FloatOps.equals(aOffset, 0)) ? Math.sqrt(aForce * aForce + bForce * bForce) : 0;
		return aOffset * bForce - aForce * bOffset < 0 ? -magnitude : magnitude;
	}

	/**
	 * Returns the applied force in {@code N}.
	 */
	public Vector3 getValue() {
		return value;
	}
	/**
	 * Returns the positional offset from the center of effect in {@code m}.
	 */
	public Vector3 getOffset() {
		return offset;
	}
}
