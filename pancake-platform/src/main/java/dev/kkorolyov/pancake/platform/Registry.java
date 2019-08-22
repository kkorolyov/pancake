package dev.kkorolyov.pancake.platform;

import dev.kkorolyov.pancake.platform.serialization.Serializer;
import dev.kkorolyov.pancake.platform.serialization.string.ResourceStringSerializer;
import dev.kkorolyov.simplefiles.Files;
import dev.kkorolyov.simplelogs.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toSet;

/**
 * A collection of loaded resources retrievable by some key.
 * @param <K> resource key type
 * @param <V> resource type
 */
public final class Registry<K, V> {
	private static final Logger LOG = Config.getLogger(Registry.class);

	private final Map<K, V> resources = new HashMap<>();
	private final ResourceStringSerializer<? extends K, ? extends V> serializer;

	/**
	 * Constructs a new registry.
	 * @param keySerializer serializes registry keys
	 * @param resourceSerializer generates registry resource serializer, invoked with {@code this}
	 */
	public Registry(Serializer<K, ? super String> keySerializer, Function<? super Registry<K, V>, ? extends Serializer<V, ? super String>> resourceSerializer) {
		serializer = new ResourceStringSerializer<>(keySerializer, resourceSerializer.apply(this), this);
	}

	/**
	 * Retrieves a resource.
	 * @param key resource key
	 * @return resource bound to {@code key}, or {@code null}
	 */
	public V get(K key) {
		return resources.get(key);
	}

	/**
	 * Retrieves all keys a resource is bound to in this registry.
	 * @param resource resource to get keys for
	 * @return all keys bound to {@code resource}
	 */
	public Collection<K> getKeys(V resource) {
		return resources.entrySet().stream()
				.filter(entry -> entry.getValue().equals(resource))
				.map(Map.Entry::getKey)
				.collect(toSet());
	}

	/**
	 * Adds a {@code key, resource} entry.
	 * @param key resource key
	 * @param resource resource value
	 */
	public void put(K key, V resource) {
		resources.put(key, resource);
	}
	/**
	 * Parses a file defining resource entries.
	 * Successfully parsed entries are added to this registry.
	 * @param path file path
	 */
	public void put(String path) {
		try (BufferedReader reader = Files.read(Resources.in(path))) {
			reader.lines()
					.forEach(line -> {
						if (serializer.accepts(line)) {
							serializer.read(line);
						} else {
							LOG.warning("Failed to parse {}", line);
						}
					});
		} catch (IOException e) {
			LOG.exception(e);
		}
	}
}
