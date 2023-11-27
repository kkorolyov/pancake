package dev.kkorolyov.pancake.platform.io;

import dev.kkorolyov.flub.function.convert.BiConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Returns a bi-directional converter between basic objects and {@code T}s.
 * See {@link BasicParsers} for details on "basic" objects.
 */
public interface ObjectConverterFactory<T> {
	/**
	 * Returns a new {@link Selector} defining the {@code converter} to return for {@code {inC, outC}} conversion combinations.
	 * The returned instance can be appended to with further {@link Selector#when(Class, Class, BiConverter)} calls.
	 */
	static <I, O> Selector when(Class<? super I> in, Class<? super O> out, BiConverter<I, O> converter) {
		return new Selector().when(in, out, converter);
	}

	/**
	 * Returns a converter between basic {@link I} objects and {@link O} subtype of {@link T}s, or {@code null} if this factory does not support {@link I} <-> {@link O} conversions.
	 */
	<I, O extends T> BiConverter<I, O> get(Class<I> inC, Class<O> outC);

	/**
	 * Returns the generic class of thing this factory provides converters for.
	 * Used to pre-filter / group providers into distinct sets of concerns.
	 * e.g. {@code Component}, {@code Object}
	 */
	Class<T> getType();

	/**
	 * Helper for configuring conditional {@link ObjectConverterFactory}s.
	 */
	final class Selector {
		private final Map<Class<?>, Map<Class<?>, BiConverter<?, ?>>> branches = new HashMap<>();

		/**
		 * Sets {@code converter} returned for {@code {inC, outC}} conversion combinations and returns this instance.
		 */
		public <I, O> Selector when(Class<? super I> inC, Class<? super O> outC, BiConverter<I, O> converter) {
			branches.computeIfAbsent(inC, k -> new HashMap<>()).put(outC, converter);
			return this;
		}

		/**
		 * Returns the best-matching converter for {@code {inC, outC}} conversion combinations, or {@code null} if none.
		 */
		public <I, O> BiConverter<I, O> get(Class<I> inC, Class<O> outC) {
			return (BiConverter<I, O>) Optional.of(branches.get(inC))
					.map(t -> t.get(outC))
					.orElse(null);
		}
	}
}
