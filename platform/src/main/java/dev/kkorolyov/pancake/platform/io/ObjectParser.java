package dev.kkorolyov.pancake.platform.io;

import dev.kkorolyov.flub.function.convert.Converter;

import java.io.InputStream;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * Parses keyed {@link T}s from an input stream.
 */
@FunctionalInterface
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

	/**
	 * Returns a parser using this parser and {@link ObjectConverters#replacing(Map)} with the replacement map found in the initially-parsed object at {@code key}.
	 * Assumes that the value at {@code key} is parsed to a {@link Map}.
	 */
	default ObjectParser<Object> preprocessReplacementMap(String key) {
		return in -> {
			Map<String, T> base = parse(in);
			Converter<Object, Object> replacer = ObjectConverters.replacing((Map<Object, Object>) base.get(key));

			return base.entrySet().stream()
					.filter(e -> !e.getKey().equals(key))
					.collect(toMap(
							Map.Entry::getKey,
							e -> replacer.convert(e.getValue())
					));
		};
	}
}
