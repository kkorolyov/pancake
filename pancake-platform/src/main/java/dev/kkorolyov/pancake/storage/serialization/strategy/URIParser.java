package dev.kkorolyov.pancake.storage.serialization.strategy;

import dev.kkorolyov.pancake.storage.serialization.ValueParser;

import java.net.URI;

/**
 * Parses values formatted as URIs.
 */
public class URIParser implements ValueParser {
	@Override
	public Object parse(String s) {
		return URI.create(s);
	}

	@Override
	public boolean accepts(String s) {
		return s.matches("^[a-zA-Z]+://.+");
	}
}
