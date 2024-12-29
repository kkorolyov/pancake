package dev.kkorolyov.pancake.platform.registry;

import dev.kkorolyov.pancake.platform.Registry;

/**
 * Returns a {@code T} given a {@link Registry}.
 */
@FunctionalInterface
@Deprecated
public interface Resource<T> {
	/**
	 * Returns a constant {@code value} irrespective of provided registry.
	 */
	static <T> Resource<T> constant(T value) {
		return registry -> value;
	}

	/**
	 * Returns a {@link T} bound to {@code registry}.
	 */
	T get(Registry<T> registry);
}
