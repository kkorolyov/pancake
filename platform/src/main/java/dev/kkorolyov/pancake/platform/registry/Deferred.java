package dev.kkorolyov.pancake.platform.registry;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.StreamSupport;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toUnmodifiableSet;

/**
 * A deferred resolution to some value.
 * @param <K> dependency key type
 * @param <V> registry resource type
 */
public final class Deferred<K, V> {
	private final Collection<K> dependencies;
	private final Function<? super Function<K, V>, ? extends V> resolver;

	/**
	 * @param value resolved value
	 * @param <K> dependency key type
	 * @param <V> value type
	 * @return deferred which resolves to a direct value
	 */
	public static <K, V> Deferred<K, V> direct(V value) {
		return new Deferred<>(emptySet(), r -> value);
	}
	/**
	 * @param dependencies registry keys on which the resolved value depends on
	 * @param resolver resolves to a dependency value by key
	 * @param <K> dependency key type
	 * @param <V> value type
	 * @return deferred which resolves to a value derived from {@code dependencies} through {@code resolver}
	 */
	public static <K, V> Deferred<K, V> derived(Iterable<K> dependencies, Function<? super Function<K, V>, ? extends V> resolver) {
		return new Deferred<>(dependencies, resolver);
	}

	private Deferred(Iterable<K> dependencies, Function<? super Function<K, V>, ? extends V> resolver) {
		this.dependencies = StreamSupport.stream(dependencies.spliterator(), false)
				.collect(toUnmodifiableSet());
		this.resolver = resolver;
	}

	/** @return keys of dependencies which must be resolved before this deferred */
	public Collection<K> getDependencies() {
		return dependencies;
	}
	/**
	 * Convenience shortcut to {@link #resolve(Function)} without dependencies.
	 * @throws IllegalStateException if this deferred declares dependencies
	 */
	public V resolve() {
		if (!dependencies.isEmpty()) {
			throw new IllegalStateException("resource must be resolved with dependencies: " + dependencies);
		}
		return resolve(null);
	}
	/**
	 * @param depResolver dependency resolutions
	 * @return resolved value
	 */
	public V resolve(Function<K, V> depResolver) {
		return resolver.apply(depResolver);
	}
}
