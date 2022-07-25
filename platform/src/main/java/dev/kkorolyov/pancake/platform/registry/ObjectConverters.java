package dev.kkorolyov.pancake.platform.registry;

import dev.kkorolyov.flub.function.convert.Converter;
import dev.kkorolyov.pancake.platform.math.Vector3;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * Returns common basic object -> typed object converters.
 */
public final class ObjectConverters {
	private ObjectConverters() {}

	public static Converter<Object, Optional<Vector3>> vector3() {
		return Converter.selective(
				t -> t instanceof Iterable,
				t -> asVector(
						StreamSupport.stream(((Iterable<?>) t).spliterator(), false)
								.map(String::valueOf)
								.map(BigDecimal::new)
								.mapToDouble(BigDecimal::doubleValue)
								.toArray()
				)
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
