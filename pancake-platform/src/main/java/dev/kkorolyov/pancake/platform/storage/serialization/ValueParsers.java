package dev.kkorolyov.pancake.platform.storage.serialization;

import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

/**
 * Provides strategies for parsing strings based on pattern.
 */
public final class ValueParsers {
	private static final Iterable<ValueParser> strategies = ServiceLoader.load(ValueParser.class);

	/**
	 * @param s string to parse
	 * @return most appropriate parser for {@code s}
	 * @throws UnsupportedOperationException if no parser for {@code s} found
	 */
	public static ValueParser getStrategy(String s) {
		return StreamSupport.stream(strategies.spliterator(), false)
				.filter(strategy -> strategy.accepts(s))
				.findFirst()
				.orElseThrow(() -> new UnsupportedOperationException("No parsing strategy for: " + s));
	}

	private ValueParsers() {}
}
