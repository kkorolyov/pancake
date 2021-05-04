package dev.kkorolyov.pancake.platform.registry;

import dev.kkorolyov.flopple.data.Graph;
import dev.kkorolyov.flopple.data.procedure.TopologicalSort;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;

import static java.util.stream.Collectors.toMap;
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

	/**
	 * Loads deferred resources into this registry.
	 * @param resources resources to load mapped by their keys
	 * @throws IllegalArgumentException if any dependency in {@code resources} exists neither in this registry nor in {@code resources}
	 */
	public void load(Map<? extends K, Deferred<K, V>> resources) {
		Map<K, Deferred<K, V>> allResources = new HashMap<>(resources);
		this.resources.forEach((key, value) -> allResources.put(key, Deferred.direct(value)));

		Map<Deferred<K, V>, K> invResources = allResources.entrySet().stream()
				.collect(toMap(
						Map.Entry::getValue,
						Map.Entry::getKey
				));
		Graph<Deferred<K, V>, Void> depGraph = allResources.values().stream()
				.collect(Collector.of(
						Graph::new,
						(g, resource) -> {
							if (resource.getDependencies().isEmpty()) {
								g.put(resource);
							} else {
								for (K dep : resource.getDependencies()) {
									Deferred<K, V> depResource = allResources.get(dep);

									if (depResource == null) {
										throw new IllegalArgumentException("No such dependency: " + dep);
									}

									g.put(depResource, resource);
								}
							}
						},
						(g, g1) -> {
							// TODO merge
							return g;
						}
				));
		for (Deferred<K, V> resource : TopologicalSort.<Deferred<K, V>>dfs().apply(depGraph)) {
			put(invResources.get(resource), resource.resolve(makeSubmap(resource.getDependencies())::get));
		}
	}
	private Map<K, V> makeSubmap(Collection<K> keys) {
		return keys.isEmpty() ? resources : resources.entrySet().stream()
				.filter(e -> keys.contains(e.getKey()))
				.collect(toMap(
						Map.Entry::getKey,
						Map.Entry::getValue
				));
	}

	@Override
	public String toString() {
		return "Registry{" +
				"resources=" + resources +
				'}';
	}
}
