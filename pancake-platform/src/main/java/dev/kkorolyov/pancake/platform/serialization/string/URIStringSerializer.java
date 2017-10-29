package dev.kkorolyov.pancake.platform.serialization.string;

import java.net.URI;

/**
 * Parses values formatted as URIs.
 */
public class URIStringSerializer extends StringSerializer<URI> {
	public URIStringSerializer() {
		super("[a-zA-Z]+://.+");
	}

	@Override
	public URI read(String s) {
		return URI.create(s);
	}
	@Override
	public String write(URI uri) {
		return uri.toString();
	}
}
