package dev.kkorolyov.pancake.platform.io;

import dev.kkorolyov.flub.function.convert.Converter;
import dev.kkorolyov.pancake.platform.math.Vector3;

import java.util.stream.StreamSupport;

/**
 * Returns common basic object -> typed object converters.
 */
public final class ObjectConverters {
	private ObjectConverters() {}

	/**
	 * Returns a converter converting number iterables to 3D vectors.
	 */
	public static Converter<Iterable<Number>, Vector3> vector3() {
		return t -> asVector(
				StreamSupport.stream(t.spliterator(), false)
						.mapToDouble(Number::doubleValue)
						.toArray()
		);
	}
	private static Vector3 asVector(double[] components) {
		return Vector3.of(
				components.length > 0 ? components[0] : 0,
				components.length > 1 ? components[1] : 0,
				components.length > 2 ? components[2] : 0
		);
	}
}
