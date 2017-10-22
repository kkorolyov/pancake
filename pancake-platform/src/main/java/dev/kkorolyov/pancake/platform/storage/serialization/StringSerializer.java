package dev.kkorolyov.pancake.platform.storage.serialization;

import java.util.Optional;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;

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

	/**
	 * @param out string to consume matches form
	 * @return result after removing all matched groups in {@code out}
	 */
	public String consume(String out) {
		return pattern.matcher(out).replaceAll("");
	}

	/**
	 * @param out string to match
	 * @return deserialized form of first matched group in {@code out}, if found
	 */
	public Optional<I> match(String out) {
		return matches(out).findFirst();
	}
	/**
	 * @param out string to match
	 * @return deserialized forms of all matched groups in {@code out}, if any
	 */
	public Stream<I> matches(String out) {
		return pattern.matcher(out).results()
				.map(MatchResult::group)
				.map(this::read);
	}

	/** @return accepted regex pattern */
	public String pattern() {
		return pattern.pattern();
	}
}
