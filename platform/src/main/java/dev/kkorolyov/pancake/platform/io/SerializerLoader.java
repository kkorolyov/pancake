package dev.kkorolyov.pancake.platform.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;

/**
 * Provides dynamically-loaded {@link Serializer} implementations.
 */
public final class SerializerLoader {
	private static final Logger log = LoggerFactory.getLogger(SerializerLoader.class);

	private static final ThreadLocal<List<Serializer>> SERIALIZERS = ThreadLocal.withInitial(() -> {
		var result = ServiceLoader.load(Serializer.class).stream()
				.map(ServiceLoader.Provider::get)
				.toList();

		log.info("loaded {} providers: {}", result.size(), result);

		return result;
	});

	private SerializerLoader() {}

	/**
	 * Returns the provider accepting subtypes of {@code c}.
	 * If no matching provider, throws {@link NoSuchElementException}.
	 * If multiple matching providers, throws {@link IllegalStateException}.
	 */
	public static <T> Serializer<T> get(Class<T> c) {
		var results = SERIALIZERS.get().stream()
				.filter(serializer -> serializer.getType().isAssignableFrom(c)).toList();

		if (results.isEmpty()) throw new NoSuchElementException("no serializer found for %s".formatted(c));
		else if (results.size() > 1) throw new IllegalStateException("multiple serializers found for %s: %s".formatted(c, results));

		return results.getFirst();
	}
}
