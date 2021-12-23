package dev.kkorolyov.pancake.platform.registry;

import dev.kkorolyov.flub.function.convert.Converter;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toMap;

/**
 * Reads deferred resources from streams.
 * @param <T> resource value type
 */
public final class ResourceReader<T> {
	private final Converter<Object, Optional<Deferred<String, T>>> converter;

	/**
	 * Constructs a new resource reader using the given converter to read resources.
	 * @param converter resource converter
	 */
	public ResourceReader(Converter<Object, Optional<Deferred<String, T>>> converter) {
		this.converter = converter;
	}

	/**
	 * Reads resources from a {@code YAML} document.
	 * @param in stream to document
	 * @return map of configured resource keys to deferreds
	 */
	public Map<String, Deferred<String, T>> fromYaml(InputStream in) {
		Map<String, Map<String, Object>> resources = new Yaml().load(in);

		return resources.entrySet().stream()
				.collect(toMap(
						Map.Entry::getKey,
						e -> converter.convert(e.getValue()).orElseThrow(() -> new IllegalArgumentException("No deferred converter matches: " + e.getValue()))
				));
	}
}
