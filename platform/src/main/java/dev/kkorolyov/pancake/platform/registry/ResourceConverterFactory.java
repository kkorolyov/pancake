package dev.kkorolyov.pancake.platform.registry;

import dev.kkorolyov.flub.function.convert.Converter;
import dev.kkorolyov.pancake.platform.io.BasicParsers;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Returns a converter of basic objects to resources.
 * See {@link BasicParsers} for details on "basic" objects.
 */
public interface ResourceConverterFactory<T> {
	/**
	 * Returns a converter of basic objects to {@link T} resources.
	 * @see Converter#selective(Predicate, Converter)
	 */
	Converter<Object, Optional<Resource<T>>> get();

	/**
	 * Returns the generic class of resource this factory provides converters for.
	 * Used to pre-filter / group providers into distinct sets of concerns.
	 * e.g. {@code Action}, {@code EntityTemplate}
	 */
	Class<T> getType();
}
