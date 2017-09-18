package dev.kkorolyov.pancake.muffin.persistence.value.strategy;

import dev.kkorolyov.pancake.muffin.persistence.value.ValueParser;

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
