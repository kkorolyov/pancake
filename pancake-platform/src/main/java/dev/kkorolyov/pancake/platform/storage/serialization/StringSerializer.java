package dev.kkorolyov.pancake.platform.storage.serialization;

import java.util.regex.Pattern;

/**
 * Serializes to strings.
 * @param <I> raw instance type
 */
public abstract class StringSerializer<I> implements Serializer<I, String> {
	private final Pattern pattern;

	/**
	 * Constructs a new string serializer.
	 * @param pattern accepted regex pattern
	 */
	public StringSerializer(String pattern) {
		this.pattern = Pattern.compile(pattern);
	}

	@Override
	public boolean accepts(String out) {
		return pattern.matcher(out).matches();
	}
}
