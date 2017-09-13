package dev.kkorolyov.pancake.muffin.persistence.value.strategy;

import dev.kkorolyov.pancake.muffin.persistence.value.ValueParser;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Parses numerical values.
 */
public class NumberParser extends ValueParser {
	@Override
	public Object parse(String s) {
		try {
			return NumberFormat.getInstance().parse(s);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean accepts(String s) {
		return s.matches("[+-]?(\\d*\\.\\d*|\\d+)");
	}
}
