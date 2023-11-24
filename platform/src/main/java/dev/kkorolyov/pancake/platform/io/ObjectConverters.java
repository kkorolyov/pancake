package dev.kkorolyov.pancake.platform.io;

import dev.kkorolyov.flub.function.convert.BiConverter;
import dev.kkorolyov.pancake.platform.math.Vector2;
import dev.kkorolyov.pancake.platform.math.Vector3;

import java.util.Arrays;
import java.util.stream.StreamSupport;

/**
 * Returns common basic object -> typed object converters.
 */
public final class ObjectConverters {
	private ObjectConverters() {}

	/**
	 * Returns a converter converting between number iterables and 2D vectors.
	 */
	public static BiConverter<Iterable<Number>, Vector2> vector2() {
		return new BiConverter<>(
				t -> asVector2(
						StreamSupport.stream(t.spliterator(), false)
								.mapToDouble(Number::doubleValue)
								.toArray()
				),
				t -> Arrays.asList(t.getX(), t.getY())
		);
	}
	/**
	 * Returns a converter converting between number iterables and 3D vectors.
	 */
	public static BiConverter<Iterable<Number>, Vector3> vector3() {
		return new BiConverter<>(
				t -> asVector3(
						StreamSupport.stream(t.spliterator(), false)
								.mapToDouble(Number::doubleValue)
								.toArray()
				),
				t -> Arrays.asList(t.getX(), t.getY(), t.getZ())
		);
	}

	private static Vector2 asVector2(double[] components) {
		return Vector2.of(
				components.length > 0 ? components[0] : 0,
				components.length > 1 ? components[1] : 0
		);
	}
	private static Vector3 asVector3(double[] components) {
		return Vector3.of(
				components.length > 0 ? components[0] : 0,
				components.length > 1 ? components[1] : 0,
				components.length > 2 ? components[2] : 0
		);
	}
}
