package dev.kkorolyov.pancake.platform.storage.serialization.strategy;

import dev.kkorolyov.pancake.platform.storage.serialization.ValueParser;

import java.math.BigDecimal;

/**
 * Parses numerical values.
 */
public class NumberParser implements ValueParser {
	@Override
	public Object parse(String s) {
			return new BigDecimal(s);
	}

	@Override
	public boolean accepts(String s) {
		return s.matches("[+-]?(\\d*\\.\\d+|\\d+)");
	}
}
