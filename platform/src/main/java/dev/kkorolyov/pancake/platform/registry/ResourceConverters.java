package dev.kkorolyov.pancake.platform.registry;

import dev.kkorolyov.flub.function.convert.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

/**
 * Provides access to dynamically-loaded {@link ResourceConverterFactory} implementations.
 */
public final class ResourceConverters {
	private static final Logger LOG = LoggerFactory.getLogger(ResourceConverters.class);

	private static final ThreadLocal<Map<Class<?>, Converter>> CONVERTERS = ThreadLocal.withInitial(HashMap::new);

	private ResourceConverters() {}

	/**
	 * Returns a converter that converts basic objects to {@code c} resources using the best-matching loaded provider.
	 */
	public static <T> Converter<Object, Optional<Resource<T>>> get(Class<T> c) {
		return CONVERTERS.get().computeIfAbsent(c, k -> {
			Collection<ResourceConverterFactory<T>> factories = ServiceLoader.load(ResourceConverterFactory.class).stream()
					.map(ServiceLoader.Provider::get)
					.filter(factory -> factory.getType() == c)
					.map(factory -> (ResourceConverterFactory<T>) factory)
					.toList();

			LOG.info("loaded {} {} providers: {}", factories.size(), c, factories);

			return Converter.reducing(factories.stream()
					.map(ResourceConverterFactory::get)
					.collect(toList()));
		});
	}
}
