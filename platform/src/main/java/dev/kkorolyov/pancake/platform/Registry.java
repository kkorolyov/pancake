package dev.kkorolyov.pancake.platform;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * A globally-accessible container of instances by type.
 */
public final class Registry<T> {
	private static final Map<Class<?>, Registry<?>> registries = new HashMap<>();

	private final Map<String, T> data = new HashMap<>();
	private final Map<T, String> reverse = new IdentityHashMap<>();

	private Registry() {}

	/**
	 * Returns the global registry for {@code c} instances.
	 * Creates an empty registry if it does not yet exist.
	 */
	public static <T> Registry<T> get(Class<T> c) {
		return (Registry<T>) registries.computeIfAbsent(c, k -> new Registry<T>());
	}

	/**
	 * Returns the instance mapped to {@code key}.
	 * Throws {@link NoSuchElementException} if no such mapping.
	 */
	public T get(String key) {
		var result = data.get(key);
		if (result == null) throw new NoSuchElementException("no such key: %s".formatted(key));
		return result;
	}
	/**
	 * Assigns {@code key} to {@code value}.
	 */
	public void put(String key, T value) {
		data.put(key, value);
		reverse.put(value, key);
	}

	/**
	 * Returns the key mapped to {@code value}.
	 * Throws {@link NoSuchElementException} if no such mapping.
	 */
	public String lookup(T value) {
		var result = reverse.get(value);
		if (result == null) throw new NoSuchElementException("no such value: %s".formatted(value));
		return result;
	}

	/**
	 * Returns the keys set in this registry.
	 */
	public Iterable<String> keys() {
		return data.keySet();
	}
}
