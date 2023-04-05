package dev.kkorolyov.pancake.platform.io;

import dev.kkorolyov.flub.function.convert.Converter;
import dev.kkorolyov.pancake.platform.math.Vector3;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Returns common basic object -> typed object converters.
 */
public final class ObjectConverters {
	private ObjectConverters() {}

	/**
	 * Returns a converter performing a deep traverse and replacement of values for the conversion input.
	 * At each level, if the input is a {@link Map} or {@link Iterable}, performs this conversion on each element, and bundles them back into the same shape data structure.
	 * If the input is a {@code String}, treats each key in {@code replacements} as a substring to replace.
	 * NOTE: Is not safe against cyclic inputs.
	 */
	public static Converter<Object, Object> replacing(Map<Object, Object> replacements) {
		return new Converter<>() {
			@Override
			public Object convert(Object t) {
				if (t instanceof Map<?, ?>) {
					return ((Map<?, ?>) t).entrySet().stream()
							.collect(Collectors.toMap(
									Map.Entry::getKey,
									e -> convert(e.getValue())
							));
				} else if (t instanceof Iterable<?>) {
					return StreamSupport.stream(((Iterable<?>) t).spliterator(), false)
							.map(this::convert)
							.toList();
				} else if (t instanceof String) {
					var replaced = replacements.entrySet().stream()
							.reduce(
									(String) t,
									(result, e) -> result.replace(e.getKey().toString(), e.getValue().toString()),
									(result, result1) -> result
							);
					try {
						return Double.parseDouble(replaced);
					} catch (NumberFormatException ignored) {}
					return replaced;
				} else {
					return replacements.getOrDefault(t, t);
				}
			}
		};
	}

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
