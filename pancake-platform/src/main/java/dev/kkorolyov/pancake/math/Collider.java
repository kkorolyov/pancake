package dev.kkorolyov.pancake.math;

/**
 * Provides methods for calculating intersection occurrence and for resolving collisions.
 */
public class Collider {
	private static final Vector mtv = new Vector();

	private static final Vector xTemp1 = new Vector();
	private static final Vector xTemp2 = new Vector();
	private static final Vector vTemp = new Vector();
	private static final Vector vDiff = new Vector();
	private static final Vector xDiff = new Vector();

	/**
	 * Checks for intersection between 2 boxes.
	 * The returned vector is shared by each intersection method in this class, so it should not be expected to retain its value after subsequent invocations of any intersection method.
	 * @param origin1 center of first box
	 * @param size1 dimensions of first box
	 * @param origin2 center of second box
	 * @param size2 dimensions of second box
	 * @return minimum translation vector to apply to first box to resolve intersection, or {@code null} if no intersection
	 */
	public static Vector intersection(Vector origin1, Vector size1, Vector origin2, Vector size2) {
		xTemp1.set(origin1);
		xTemp1.sub(size1, .5f);
		xTemp2.set(origin2);
		xTemp2.sub(size2, .5f);

		float xOverlap = overlap(origin1.getX(), origin1.getX() + size1.getX(), origin2.getX(), origin2.getX() + size2.getX());
		float yOverlap = overlap(origin1.getY(), origin1.getY() + size1.getY(), origin2.getY(), origin2.getY() + size2.getY());
		float zOverlap = overlap(origin1.getZ(), origin1.getZ() + size1.getZ(), origin2.getZ(), origin2.getZ() + size2.getZ());

		if (xOverlap != 0 && yOverlap != 0 && zOverlap != 0) {
			float xDiff = Math.abs(xOverlap), yDiff = Math.abs(yOverlap), zDiff = Math.abs(zOverlap);

			if (xDiff <= yDiff && xDiff <= zDiff) mtv.set(xOverlap, 0, 0);
			else if (yDiff <= xDiff && yDiff <= zDiff) mtv.set(0, yOverlap, 0);
			else mtv.set(0, 0, zOverlap);

			return mtv;
		} else {
			return null;
		}
	}
	private static float overlap(float x1, float x2, float y1, float y2) {	// SAT
		if (x1 == x2 && x1 == y1 && x1 == y2) return Float.MAX_VALUE;

		return (x2 >= y1 && y2 >= x1)
					 ? (x2 < y2) ? y1 - x2 : y2 - x1	// Negative if 1st line overlaps from left, positive if from right
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
	public static Vector intersection(Vector origin1, float radius1, Vector origin2, float radius2) {
		mtv.set(origin2);
		mtv.sub(origin1);	// Vector from origin1 to origin2

		float overlap = mtv.getMagnitude() - (radius1 + radius2);

		if (overlap < 0) {
			mtv.normalize();	// Retain only direction
			mtv.scale(overlap); // Move origin1 away from origin2 by overlap amount

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
	public static Vector intersection(Vector origin1, Vector size1, Vector origin2, float radius2) {
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
	public static void elasticCollide(Vector origin1, Vector velocity1, float mass1, Vector origin2, Vector velocity2, float mass2) {
		vTemp.set(velocity1);

		applyElastic(origin1, velocity1, mass1, origin2, velocity2, mass2);
		applyElastic(origin2, velocity2, mass2, origin1, vTemp, mass2);
	}
	// v1' = v1 - 2(m2)/(m1 + m2) * ((v1 - v2) * (x1 - x2))/(||x1 - x2||^2) * (x1 - x2)
	private static void applyElastic(Vector origin1, Vector velocity1, float mass1, Vector origin2, Vector velocity2, float mass2) {
		vDiff.set(velocity1);
		vDiff.sub(velocity2);

		xDiff.set(origin1);
		xDiff.sub(origin2);

		float dotNumerator = vDiff.dot(xDiff);
		float dotDenominator = xDiff.dot(xDiff);

		xDiff.scale(((2 * mass2) / (mass1 + mass2)) * (dotNumerator / dotDenominator));

		velocity1.sub(xDiff);
	}
}
