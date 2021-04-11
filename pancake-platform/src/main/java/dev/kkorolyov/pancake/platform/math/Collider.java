package dev.kkorolyov.pancake.platform.math;

/**
 * Provides methods for calculating intersection occurrence and for resolving collisions.
 */
// TODO Make instantiable
public final class Collider {
	private static final Vector3 mtv = Vectors.create(0, 0, 0);

	private static final Vector3 xTemp1 = Vectors.create(0, 0, 0);
	private static final Vector3 xTemp2 = Vectors.create(0, 0, 0);
	private static final Vector3 vTemp = Vectors.create(0, 0, 0);
	private static final Vector3 vDiff = Vectors.create(0, 0, 0);
	private static final Vector3 xDiff = Vectors.create(0, 0, 0);

	private Collider() {}

	/**
	 * Checks for intersection between 2 boxes.
	 * The returned vector is shared by each intersection method in this class, so it should not be expected to retain its value after subsequent invocations of any intersection method.
	 * @param origin1 center of first box
	 * @param size1 dimensions of first box
	 * @param origin2 center of second box
	 * @param size2 dimensions of second box
	 * @return minimum translation vector to apply to first box to resolve intersection, or {@code null} if no intersection
	 */
	public static Vector3 intersection(Vector3 origin1, Vector3 size1, Vector3 origin2, Vector3 size2) {
		xTemp1.set(origin1);
		xTemp1.add(size1, -.5);  // Lower-left vertex
		xTemp2.set(origin2);
		xTemp2.add(size2, -.5);

		double xOverlap = overlap(xTemp1.getX(), xTemp1.getX() + size1.getX(), xTemp2.getX(), xTemp2.getX() + size2.getX());
		double yOverlap = overlap(xTemp1.getY(), xTemp1.getY() + size1.getY(), xTemp2.getY(), xTemp2.getY() + size2.getY());
		double zOverlap = overlap(xTemp1.getZ(), xTemp1.getZ() + size1.getZ(), xTemp2.getZ(), xTemp2.getZ() + size2.getZ());

		if (xOverlap != 0 && yOverlap != 0 && zOverlap != 0) {
			double xDiff = Math.abs(xOverlap), yDiff = Math.abs(yOverlap), zDiff = Math.abs(zOverlap);

			mtv.setX(0);
			mtv.setY(0);
			mtv.setZ(0);

			if (xDiff <= yDiff && xDiff <= zDiff) {
				mtv.setX(xOverlap);
			} else if (yDiff <= xDiff && yDiff <= zDiff) {
				mtv.setY(yOverlap);
			} else {
				mtv.setZ(zOverlap);
			}

			return mtv;
		} else {
			return null;
		}
	}
	private static double overlap(double x1, double x2, double y1, double y2) {  // SAT
		if (x1 == x2 && x1 == y1 && x1 == y2) return Double.MAX_VALUE;

		return (x2 >= y1 && y2 >= x1)
				? (x2 < y2) ? y1 - x2 : y2 - x1  // Negative if 1st line overlaps from left, positive if from right
				: 0;
	}
	/**
	 * Checks for intersection between 2 spheres.
	 * The returned vector is shared by each intersection method in this class, so it should not be expected to retain its value after subsequent invocations of any intersection method.
	 * @param origin1 center of 1st sphere
	 * @param radius1 radius of 1st sphere
	 * @param origin2 center of 2nd sphere
	 * @param radius2 radius of 2nd sphere
	 * @return minimum translation vector to apply to first sphere to resolve intersection, or {@code null} if no intersection
	 */
	public static Vector3 intersection(Vector3 origin1, double radius1, Vector3 origin2, double radius2) {
		mtv.set(origin2);
		mtv.add(origin1, -1);  // Vector from origin1 to origin2

		double mtvMag = VectorMath.magnitude(mtv);
		double overlap = mtvMag - (radius1 + radius2);

		if (overlap < 0) {
			mtv.scale(overlap / mtvMag); // Move origin1 away from origin2 by overlap amount

			return mtv;
		} else {
			return null;
		}
	}
	/**
	 * Checks for intersection between a box and a sphere.
	 * @param origin1 origin of 1st object (box)
	 * @param size1 size of 1st object (box)
	 * @param origin2 origin of 2nd object (sphere)
	 * @param radius2 radius of 2nd object (sphere)
	 * @return minimum translation vector to apply to first object to resolve intersection, or {@code null} if no intersection
	 */
	public static Vector3 intersection(Vector3 origin1, Vector3 size1, Vector3 origin2, double radius2) {
		// TODO
		return null;
	}

	/**
	 * Alters the velocities of 2 objects to resemble an elastic collision.
	 * @param origin1 center of 1st object
	 * @param velocity1 velocity of 1st object
	 * @param mass1 mass of 1st object
	 * @param origin2 center of 2nd object
	 * @param velocity2 velocity of 2nd object
	 * @param mass2 mass of 2nd object
	 */
	public static void elasticCollide(Vector3 origin1, Vector3 velocity1, double mass1, Vector3 origin2, Vector3 velocity2, double mass2) {
		vTemp.set(velocity1);

		applyElastic(origin1, velocity1, mass1, origin2, velocity2, mass2);
		applyElastic(origin2, velocity2, mass2, origin1, vTemp, mass2);
	}
	// v1' = v1 - 2(m2)/(m1 + m2) * ((v1 - v2) * (x1 - x2))/(||x1 - x2||^2) * (x1 - x2)
	private static void applyElastic(Vector3 origin1, Vector3 velocity1, double mass1, Vector3 origin2, Vector3 velocity2, double mass2) {
		vDiff.set(velocity1);
		vDiff.add(velocity2, -1);

		xDiff.set(origin1);
		xDiff.add(origin2, -1);

		xDiff.scale(((2 * mass2) / (mass1 + mass2)) * (VectorMath.dot(vDiff, xDiff) / VectorMath.dot(xDiff, xDiff)));

		velocity1.add(xDiff, -1);
	}
}
