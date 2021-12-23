package dev.kkorolyov.pancake.platform.plugin;

import dev.kkorolyov.flub.function.convert.Converter;
import dev.kkorolyov.pancake.platform.registry.Deferred;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * Interfaces with implementations provided by external sources.
 * Some plugins are expected to provide exactly one implementation at runtime, others can provide zero to many.
 * This class provides access to plugin interfaces defined by the platform.
 */
public final class Plugins {
	private static final Logger LOG = LoggerFactory.getLogger(Plugins.class);

	private static final ThreadLocal<Map<Class<? extends DeferredConverterFactory<?>>, Converter>> DEFERRED_CONVERTERS = ThreadLocal.withInitial(HashMap::new);

	/**
	 * @param c deferred converter factory type to reduce
	 * @param <T> deferred converter factory type
	 * @return reduced converter from converters supplied by all providers of type {@code c} bound to the current thread
	 */
	public static <T> Converter<Object, Optional<Deferred<String, T>>> deferredConverter(Class<? extends DeferredConverterFactory<T>> c) {
		return DEFERRED_CONVERTERS.get().computeIfAbsent(c, k -> Converter.reducing(loadAll(c).stream()
				.map(DeferredConverterFactory::get)
				.collect(toSet())
		));
	}

	private static <T> T loadOne(Class<T> c) {
		T t = ServiceLoader.load(c).findFirst().orElseThrow(() -> new IllegalStateException("No " + c + " provider found"));
		LOG.info("loaded {}: {}", c, t);
		return t;
	}
	private static <T> Collection<T> loadAll(Class<? extends T> c) {
		Collection<T> ts = ServiceLoader.load(c).stream().map(ServiceLoader.Provider::get).collect(toList());
		LOG.info("loaded {}: {}", c, ts);
		return ts;
	}
}
