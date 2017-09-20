package dev.kkorolyov.pancake.storage.serialization;

import dev.kkorolyov.pancake.storage.serialization.strategy.MapParser;
import dev.kkorolyov.pancake.storage.serialization.strategy.NumberParser;
import dev.kkorolyov.pancake.storage.serialization.strategy.StringParser;
import dev.kkorolyov.pancake.storage.serialization.strategy.URIParser;

import java.util.Arrays;
import java.util.Collection;

/**
 * Provides strategies for parsing strings based on pattern.
 */
public final class ValueParsers {
	private static final Collection<ValueParser> strategies = Arrays.asList(
			new URIParser(),
			new NumberParser(),
			new StringParser(),
			new MapParser()
	);

	/**
	 * @param s string to parse
	 * @return most appropriate parser for {@code s}
	 * @throws UnsupportedOperationException if no parser for {@code s} found
	 */
	public static ValueParser getStrategy(String s) {
		return strategies.stream()
				.filter(strategy -> strategy.accepts(s))
				.findFirst()
				.orElseThrow(() -> new UnsupportedOperationException("No parsing strategy for: " + s));
	}

	private ValueParsers() {}
}
