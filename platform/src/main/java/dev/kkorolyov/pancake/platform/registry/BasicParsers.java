package dev.kkorolyov.pancake.platform.registry;

import org.yaml.snakeyaml.Yaml;

/**
 * Returns {@link ObjectParser}s parsing to a "basic" object format, like {@code String}, {@code Number}, {@code Iterable}, or {@code Map}.
 */
public final class BasicParsers {
	private BasicParsers() {}

	/**
	 * Returns a parser parsing objects from YAML.
	 */
	public static ObjectParser<Object> yaml() {
		return in -> new Yaml().load(in);
	}
}
