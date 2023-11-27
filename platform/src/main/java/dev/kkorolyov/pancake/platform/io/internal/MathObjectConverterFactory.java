package dev.kkorolyov.pancake.platform.io.internal;

import dev.kkorolyov.flub.function.convert.BiConverter;
import dev.kkorolyov.pancake.platform.io.ObjectConverterFactory;
import dev.kkorolyov.pancake.platform.math.Vector2;
import dev.kkorolyov.pancake.platform.math.Vector3;

import java.util.Arrays;
import java.util.stream.StreamSupport;

/**
 * {@link ObjectConverterFactory} for math objects, including vectors.
 */
public final class MathObjectConverterFactory implements ObjectConverterFactory<Object> {
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

	@Override
	public <I, O> BiConverter<I, O> get(Class<I> inC, Class<O> outC) {
		return ObjectConverterFactory
				.when(Iterable.class, Vector2.class, new BiConverter<Iterable<Number>, Vector2>(
						t -> asVector2(
								StreamSupport.stream(t.spliterator(), false)
										.mapToDouble(Number::doubleValue)
										.toArray()
						),
						t -> Arrays.asList(t.getX(), t.getY())
				))
				.when(Iterable.class, Vector3.class, new BiConverter<Iterable<Number>, Vector3>(
								t -> asVector3(
										StreamSupport.stream(t.spliterator(), false)
												.mapToDouble(Number::doubleValue)
												.toArray()
								),
								t -> Arrays.asList(t.getX(), t.getY(), t.getZ())
						)
				)
				.get(inC, outC);
	}

	@Override
	public Class<Object> getType() {
		return Object.class;
	}
}
