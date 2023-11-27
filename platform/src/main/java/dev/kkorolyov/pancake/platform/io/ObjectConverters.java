package dev.kkorolyov.pancake.platform.io;

import dev.kkorolyov.flub.function.convert.BiConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

/**
 * Provides access to dynamically-loaded {@link ObjectConverterFactory} implementations.
 */
public final class ObjectConverters {
	private static final Logger log = LoggerFactory.getLogger(ObjectConverters.class);

	private static final ThreadLocal<Map<Class<?>, List<ObjectConverterFactory>>> factories = ThreadLocal.withInitial(() -> {
				Map<Class<?>, List<ObjectConverterFactory>> result = ServiceLoader.load(ObjectConverterFactory.class).stream()
						.map(ServiceLoader.Provider::get)
						.collect(Collectors.groupingBy(ObjectConverterFactory::getType));

				result.forEach((c, providers) -> log.info("loaded {} {} providers: {}", providers.size(), c, providers));

				return result;
			}
	);
	private ObjectConverters() {}

	/**
	 * Returns the {@code c}-type converter supporting {@code inC <-> outC} conversions.
	 * If no matching converter found, throws {@link NoSuchElementException}.
	 */
	public static <T, I, O extends T> BiConverter<I, O> get(Class<T> c, Class<I> inC, Class<O> outC) {
		return factories.get().getOrDefault(c, Collections.emptyList()).stream()
				.map(t -> t.get(inC, outC))
				.filter(Objects::nonNull)
				.findFirst()
				.orElseThrow(() -> new NoSuchElementException("no [%s]-type provider found for converting between [%s] and [%s]".formatted(c, inC, outC)));
	}
}
