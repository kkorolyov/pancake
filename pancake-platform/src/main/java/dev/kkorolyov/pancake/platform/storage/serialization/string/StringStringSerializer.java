package dev.kkorolyov.pancake.platform.storage.serialization.string;

import dev.kkorolyov.pancake.platform.storage.serialization.StringSerializer;

/**
 * Parses text in "".
 */
public class StringStringSerializer extends StringSerializer<String> {	// "StringString"
	public StringStringSerializer() {
		super("\".*\"");
	}

	@Override
	public String read(String s) {
		return s.substring(1, s.length() - 1);
	}
	@Override
	public String write(String string) {
		return string;
	}
}
