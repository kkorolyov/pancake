package dev.kkorolyov.pancake.muffin.persistence.value;

import dev.kkorolyov.pancake.muffin.persistence.value.strategy.MapParser;
import dev.kkorolyov.pancake.muffin.persistence.value.strategy.NumberParser;
import dev.kkorolyov.pancake.muffin.persistence.value.strategy.StringParser;
import dev.kkorolyov.pancake.muffin.persistence.value.strategy.URIParser;

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
	 * @throws UnsupportedOperationException if no parser for {@code s}
	 */
	public static ValueParser getStrategy(String s) {
		return strategies.stream()
				.filter(strategy -> strategy.accepts(s))
				.findFirst()
				.orElseThrow(() -> new UnsupportedOperationException("No parsing strategy for: " + s));
	}

	private ValueParsers() {}
}
