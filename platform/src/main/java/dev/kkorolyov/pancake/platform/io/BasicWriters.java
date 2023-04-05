package dev.kkorolyov.pancake.platform.io;

import org.yaml.snakeyaml.Yaml;

import java.io.OutputStreamWriter;

/**
 * Returns {@link ObjectWriter}s capable of writing "basic" object formats, like {@code String}, {@code Number}, {@code Iterable}, or {@code Map}.
 */
public final class BasicWriters {
	private BasicWriters() {}

	public static ObjectWriter<Object> yaml() {
		return ((out, t) -> new Yaml().dump(t, new OutputStreamWriter(out)));
	}
}
