package dev.kkorolyov.pancake.muffin.persistence.value;

/**
 * Parses a value to a certain type.
 */
public abstract class ValueParser {
	/** @return object parsed from {@code s} */
	public abstract Object parse(String s);

	/** @return {@code true} if able to parse {@code s} */
	public abstract boolean accepts(String s);
}
