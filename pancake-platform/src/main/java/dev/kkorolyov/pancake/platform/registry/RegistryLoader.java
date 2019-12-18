package dev.kkorolyov.pancake.platform.registry;

import dev.kkorolyov.pancake.platform.Config;
import dev.kkorolyov.simplefuncs.convert.Converter;
import dev.kkorolyov.simpleprops.Properties;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Loads resources into a {@link Registry}.
 * @param <K> resource key type
 * @param <V> resource type
 */
@FunctionalInterface
public interface RegistryLoader<K, V> {
	/**
	 * Generates a registry loader for resources defined as properties.
	 * @param props properties to load fromm
	 * @param resourceReader generates resource reader, invoked with loaded registry
	 * @param <V> resource type
	 * @return registry loader for resources defined as properties
	 */
	static <V> RegistryLoader<String, V> fromProperties(
			Properties props,
			Function<? super Registry<? super String, ? extends V>, ? extends Converter<String, Optional<? extends V>>> resourceReader
	) {
		return registry -> {
			Converter<? super String, ? extends Optional<? extends V>> reader = resourceReader.apply(registry);
			for (Map.Entry<String, String> prop : props) {
				reader.convert(prop.getValue())
						.ifPresentOrElse(
								resource -> registry.put(prop.getKey(), resource),
								() -> Config.getLogger(RegistryLoader.class).warning("Failed to parse resource: {}", prop.getValue())
						);
			}
		};
	}

	/**
	 * Applies loaded resources to a given registry.
	 * @param registry registry to load
	 */
	void load(Registry<? super K, V> registry);
}
