package dev.kkorolyov.pancake.platform.math;

/**
 * Performs computations involving vectors.
 * @deprecated prefer methods in dedicated Vector interfaces.
 */
@Deprecated
public final class VectorMath {
	private VectorMath() {}

	/** @return unit vector of {@code vector} (vector with magnitude {@code 1} and same direction as {@code vector}) */
	public static Vector2 unit(Vector2 vector) {
		double magnitude = magnitude(vector);
		return magnitude == 0 ? Vectors.create(0, 0) : Vectors.create(vector.getX() / magnitude, vector.getY() / magnitude);
	}
	/** @return unit vector of {@code vector} (vector with magnitude {@code 1} and same direction as {@code vector}) */
	public static Vector3 unit(Vector3 vector) {
		double magnitude = magnitude(vector);
		return magnitude == 0 ? Vectors.create(0, 0, 0) : Vectors.create(vector.getX() / magnitude, vector.getY() / magnitude, vector.getZ() / magnitude);
	}

	/** @return magnitude of {@code vector} */
	public static double magnitude(Vector2 vector) {
		return Math.sqrt(dot(vector, vector));
	}
	/** @return magnitude of {@code vector} */
	public static double magnitude(Vector3 vector) {
		return Math.sqrt(dot(vector, vector));
	}

	/** @return dot product of {@code vector} and {@code other} */
	public static double dot(Vector2 vector, Vector2 other) {
		return vector.getX() * other.getX() + vector.getY() * other.getY();
	}
	/** @return dot product of {@code vector} and {@code other} */
	public static double dot(Vector3 vector, Vector3 other) {
		return vector.getX() * other.getX() + vector.getY() * other.getY() + vector.getZ() * other.getZ();
	}

	/** @return Euclidean distance between {@code vector} and {@code other} */
	public static double distance(Vector2 vector, Vector2 other) {
		return Math.sqrt(
				Math.pow(vector.getX() - other.getX(), 2) +
						Math.pow(vector.getY() - other.getY(), 2)
		);
	}
	/** @return Euclidean distance between {@code vector} and {@code other} */
	public static double distance(Vector3 vector, Vector3 other) {
		return Math.sqrt(
				Math.pow(vector.getX() - other.getX(), 2) +
						Math.pow(vector.getY() - other.getY(), 2) +
						Math.pow(vector.getZ() - other.getZ(), 2)
		);
	}

	/** @return projection of {@code other} on {@code vector} */
	public static Vector2 projection(Vector2 vector, Vector2 other) {
		double scale = dot(vector, other) / dot(vector, vector);
		return Vectors.create(vector.getX() * scale, vector.getY() * scale);
	}
	/** @return projection of {@code other} on {@code vector} */
	public static Vector3 projection(Vector3 vector, Vector3 other) {
		double scale = dot(vector, other) / dot(vector, vector);
		return Vectors.create(vector.getX() * scale, vector.getY() * scale, vector.getZ() * scale);
	}
}
