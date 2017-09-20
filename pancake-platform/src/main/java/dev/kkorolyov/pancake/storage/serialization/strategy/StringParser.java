package dev.kkorolyov.pancake.storage.serialization.strategy;

import dev.kkorolyov.pancake.storage.serialization.ValueParser;

/**
 * Parses text in "".
 */
public class StringParser implements ValueParser {
	@Override
	public Object parse(String s) {
		return s.substring(1, s.length() - 1);
	}

	@Override
	public boolean accepts(String s) {
		return s.matches("^\".*\"$");
	}
}
