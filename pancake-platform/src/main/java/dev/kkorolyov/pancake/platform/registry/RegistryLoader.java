package dev.kkorolyov.pancake.platform.registry;

import dev.kkorolyov.flopple.function.convert.Converter;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.Properties;
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
			// FIXME This is not in-order due to hashtable
			for (Map.Entry<Object, Object> prop : props.entrySet()) {
				reader.convert(prop.getValue().toString())
						.ifPresentOrElse(
								resource -> registry.put(prop.getKey().toString(), resource),
								() -> LoggerFactory.getLogger(RegistryLoader.class).warn("Failed to parse resource: {}", prop.getValue())
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
