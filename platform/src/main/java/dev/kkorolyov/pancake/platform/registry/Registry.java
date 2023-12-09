package dev.kkorolyov.pancake.platform.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * A collection of keyed resources lazily evaluated against this container.
 * Caches results upon first {@link #get(String)}.
 * Invalidates the cache upon modification.
 * @param <T> resource type
 */
public final class Registry<T> {
	private final Map<String, Resource<T>> resources = new HashMap<>();
	private final Map<String, T> cache = new HashMap<>();

	/**
	 * Returns the resource bound to {@code key}, else {@code null}.
	 */
	public T get(String key) {
		var result = cache.get(key);
		if (result == null) {
			var resource = resources.get(key);
			if (resource != null) {
				result = resource.get(this);
				cache.put(key, result);
			}
		}
		return result;
	}

	/**
	 * Adds a {@code {key, resources}} entry to this registry.
	 */
	public void put(String key, Resource<T> resource) {
		resources.put(key, resource);
		cache.clear();
	}
	/**
	 * Adds all {@code resources} to this registry.
	 */
	public void putAll(Map<String, ? extends Resource<T>> resources) {
		this.resources.putAll(resources);
		cache.clear();
	}

	/**
	 * Runs {@code op} on each {@code (key, resource)} pair in this registry.
	 */
	public void forEach(BiConsumer<? super String, ? super T> op) {
		for (String key : resources.keySet()) {
			op.accept(key, get(key));
		}
	}

	@Override
	public String toString() {
		Map<String, T> evaluated = new HashMap<>(resources.size());
		forEach(evaluated::put);
		return evaluated.toString();
	}
}
