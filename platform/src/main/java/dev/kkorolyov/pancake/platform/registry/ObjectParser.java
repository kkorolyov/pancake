package dev.kkorolyov.pancake.platform.registry;

import dev.kkorolyov.flub.function.convert.Converter;

import java.io.InputStream;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * Parses keyed {@link T}s from an input stream.
 */
public interface ObjectParser<T> {
	/**
	 * Returns keyed {@link T}s parsed from {@code in}.
	 */
	Map<String, T> parse(InputStream in);

	/**
	 * Returns a parser that parses using this parser and converts parsed values with {@code converter}.
	 */
	default <R> ObjectParser<R> andThen(Converter<? super T, ? extends R> converter) {
		return in -> parse(in).entrySet().stream()
				.collect(toMap(
						Map.Entry::getKey,
						e -> converter.convert(e.getValue())
				));
	}
}
