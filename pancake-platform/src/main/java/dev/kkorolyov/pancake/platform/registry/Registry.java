package dev.kkorolyov.pancake.platform.registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toSet;

/**
 * A collection of loaded resources retrievable by some key.
 * @param <K> resource key type
 * @param <V> resource type
 */
public final class Registry<K, V> {
	private final Map<K, V> resources = new HashMap<>();

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
}
