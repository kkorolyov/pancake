package dev.kkorolyov.pancake.math;

/**
 * Provides methods for calculating intersection occurrence and for resolving collisions.
 */
public class Collider {
	/**
	 * Checks for intersection between 2 boxes.
	 * @param origin1 origin vertex of first box
	 * @param direction1 diagonally-opposite vertex of first box
	 * @param origin2 origin vertex of second box
	 * @param direction2 diagonally-opposite vertex of second box
	 * @return minimum translation vector to resolve intersection, or {@code null} if no intersection
	 */
	public static Vector intersection(Vector origin1, Vector direction1, Vector origin2, Vector direction2) {
		float xOverlap = overlap(origin1.getX(), origin1.getX() + direction1.getX(), origin2.getX(), origin2.getX() + direction2.getX());
		float yOverlap = overlap(origin1.getY(), origin1.getY() + direction1.getY(), origin2.getY(), origin2.getY() + direction2.getY());
		float zOverlap = overlap(origin1.getZ(), origin1.getZ() + direction1.getZ(), origin2.getZ(), origin2.getZ() + direction2.getZ());

		if (xOverlap == 0 || yOverlap == 0 || zOverlap == 0) return null;

		float xDiff = Math.abs(xOverlap), yDiff = Math.abs(yOverlap), zDiff = Math.abs(zOverlap);

		if (xDiff <= yDiff && xDiff <= zDiff) return new Vector(xOverlap, 0, 0);
		else if (yDiff <= xDiff && yDiff <= zDiff) return new Vector(0, yOverlap, 0);
		else return new Vector(0, 0, zOverlap);
	}
	private static float overlap(float x1, float x2, float y1, float y2) {	// SAT
		if (x1 == x2 && x1 == y1 && x1 == y2) return Float.MAX_VALUE;

		return (x2 >= y1 && y2 >= x1)
					 ? (x2 < y2) ? y1 - x2 : y2 - x1	// Negative if 1st line overlaps from left, positive if from right
					 : 0;
	}
}
