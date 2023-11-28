package dev.kkorolyov.pancake.platform.io;

import dev.kkorolyov.flub.function.convert.Converter;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Converts between objects and simple serializable structures.
 * Implementations can make use of the {@code first()} and {@code select()} static helpers to define conversion method conditions.
 */
public interface Structizer {
	/**
	 * Returns a converter invoking {@code converter} for instances matching {@code c}.
	 */
	static <T, U extends T, R> Converter<T, Optional<R>> get(Class<U> c, Converter<? super U, ? extends R> converter) {
		return Converter.selective(
				t -> c.isAssignableFrom(t.getClass()),
				((Converter<Object, U>) c::cast).andThen(converter)
		);
	}

	/**
	 * Returns a function mapper using the first-matched of {@code mappers}.
	 * If no mapper matches, returns a mapper to {@code null}.
	 * @see #select(Class, Function)
	 */
	@SafeVarargs
	static <T, U extends T, R> Function<T, R> first(ToStructFunction<? extends U, ?>... mappers) {
		return t -> Arrays.stream(mappers)
				.map(mapper -> ((Function) mapper).apply(t))
				.filter(Objects::nonNull)
				.map(result -> (R) result)
				.findFirst()
				.orElse(null);
	}
	/**
	 * Returns a function mapper using {@code mapper} for {@code c} instances, and mapping to {@code null} otherwise.
	 * @see #first(ToStructFunction[])
	 */
	static <T, R> ToStructFunction<T, R> select(Class<T> c, Function<T, R> mapper) {
		return t -> c.isInstance(t) ? mapper.apply(t) : null;
	}

	/**
	 * Returns a function mapper using the first-matched of {@code mappers}.
	 * If no mapper matches, returns a mapper to {@code null}.
	 * @see #select(Class, Class, Function)
	 */
	@SafeVarargs
	static <T, R> Function<T, R> first(FromStructFunction<T, ?>... mappers) {
		return Arrays.stream(mappers)
				.filter(Objects::nonNull)
				.map(t -> (Function<T, R>) t)
				.findFirst()
				.orElse(t -> null);
	}
	/**
	 * Returns a function mapper using {@code mapper} if {@code selectC} is a subtype of {@code c}, and {@code null} otherwise.
	 * @see #first(FromStructFunction[])
	 */
	static <T, R> FromStructFunction<T, R> select(Class<?> selectC, Class<R> c, Function<T, R> mapper) {
		return c.isAssignableFrom(selectC) ? mapper::apply : null;
	}

	/**
	 * Returns {@code o} as a simple serializable structure, or an empty optional if this instance does not support such conversion.
	 */
	Optional<Object> toStruct(Object o);
	/**
	 * Returns simple serializable structure {@code o} as a {@code c} instance, or an empty optional if this instance does not support such conversion.
	 */
	<T> Optional<T> fromStruct(Class<T> c, Object o);

	@FunctionalInterface
	interface ToStructFunction<T, R> extends Function<T, R> {}

	@FunctionalInterface
	interface FromStructFunction<T, R> extends Function<T, R> {}
}
