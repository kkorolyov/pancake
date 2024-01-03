package dev.kkorolyov.pancake.platform.io.internal;

import dev.kkorolyov.pancake.platform.io.Structizer;
import dev.kkorolyov.pancake.platform.math.Matrix4;
import dev.kkorolyov.pancake.platform.math.Vector2;
import dev.kkorolyov.pancake.platform.math.Vector3;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

/**
 * {@link Structizer} for math objects, such as vectors.
 */
public final class MathStructizer implements Structizer {
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

	private static Matrix4 asMatrix4(double[] components) {
		return Matrix4.of(
				components[0], components[1], components[2], components[3],
				components[4], components[5], components[6], components[7],
				components[8], components[9], components[10], components[11],
				components[12], components[13], components[14], components[15]
		);
	}

	@Override
	public Optional<Object> toStruct(Object o) {
		return Optional.of(o)
				.map(Structizer.first(
						Structizer.select(Vector3.class, t -> Arrays.asList(t.getX(), t.getY(), t.getZ())),
						Structizer.select(Vector2.class, t -> Arrays.asList(t.getX(), t.getY())),

						Structizer.select(Matrix4.class, t -> Arrays.asList(
								t.getXx(), t.getXy(), t.getXz(), t.getXw(),
								t.getYx(), t.getYy(), t.getYz(), t.getYw(),
								t.getZx(), t.getZy(), t.getZz(), t.getZw(),
								t.getWx(), t.getWy(), t.getWz(), t.getWw()
						))
				));
	}

	@Override
	public <T> Optional<T> fromStruct(Class<T> c, Object o) {
		return Optional.of(o)
				.filter(t -> t instanceof Collection<?> && ((Collection<?>) t).stream().allMatch(t1 -> t1 instanceof Number))
				.map(t -> (Collection<Number>) t)
				.map(Structizer.first(
						Structizer.select(c, Vector3.class, t -> asVector3(t.stream().mapToDouble(Number::doubleValue).toArray())),
						Structizer.select(c, Vector2.class, t -> asVector2(t.stream().mapToDouble(Number::doubleValue).toArray())),

						Structizer.select(c, Matrix4.class, t -> asMatrix4(t.stream().mapToDouble(Number::doubleValue).toArray()))
				));
	}
}
