package dev.kkorolyov.pancake.platform.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static java.util.stream.Collectors.toMap;

/**
 * A collection of keyed resources lazily evaluated against their owning container.
 * @param <T> resource type
 */
public final class Registry<T> {
	private final Map<String, Resource<T>> resources = new HashMap<>();

	/**
	 * Returns the resource bound to {@code key}, else {@code null}.
	 */
	public T get(String key) {
		Resource<T> resource = resources.get(key);
		return resource == null ? null : resource.get(this);
	}

	/**
	 * Adds a {@code {key, resources}} entry to this registry.
	 */
	public void put(String key, Resource<T> resource) {
		resources.put(key, resource);
	}
	/**
	 * Adds all {@code resources} to this registry.
	 */
	public void putAll(Map<String, ? extends Resource<T>> resources) {
		this.resources.putAll(resources);
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
		Map<String, T> evaluated = resources.entrySet().stream()
				.collect(toMap(
						Map.Entry::getKey,
						e -> e.getValue().get(this)
				));

		return evaluated.toString();
	}
}
