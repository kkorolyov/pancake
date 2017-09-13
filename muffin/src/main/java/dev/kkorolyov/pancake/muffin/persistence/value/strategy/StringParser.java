package dev.kkorolyov.pancake.muffin.persistence.value.strategy;

import dev.kkorolyov.pancake.muffin.persistence.value.ValueParser;

/**
 * Parses text in "".
 */
public class StringParser extends ValueParser {
	@Override
	public Object parse(String s) {
		return s.substring(1, s.length() - 1);
	}

	@Override
	public boolean accepts(String s) {
		return s.matches("^\".*\"$");
	}
}
