package dev.kkorolyov.pancake.platform.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Provides access to dynamically-loaded {@link Structizer} implementations.
 */
public final class Structizers {
	private static final Logger log = LoggerFactory.getLogger(Structizers.class);

	private static final ThreadLocal<List<Structizer>> STRUCTIZERS = ThreadLocal.withInitial(() -> {
				var result = ServiceLoader.load(Structizer.class).stream()
						.map(ServiceLoader.Provider::get)
						.collect(Collectors.toList());

				log.info("loaded {} providers: {}", result.size(), result);

				return result;
			}
	);
	private static final ThreadLocal<Set<Structizer>> RUNNING_STRUCTIZERS = ThreadLocal.withInitial(HashSet::new);

	private Structizers() {}

	/**
	 * Returns {@code o} as a simple serializable structure.
	 * Throws {@link IllegalArgumentException} if no {@link Structizer} provider can handle this.
	 */
	public static Object toStruct(Object o) {
		return useStructizer(structizer -> structizer.toStruct(o), () -> new IllegalArgumentException("no structizer found for %s -> struct".formatted(o)));
	}
	/**
	 * Returns simple serializable structure {@code o} as a {@code c} instance.
	 * Throws {@link IllegalArgumentException} if no {@link Structizer} provider can handle this.
	 */
	public static <T> T fromStruct(Class<T> c, Object o) {
		return useStructizer(structizer -> structizer.fromStruct(c, o), () -> new IllegalArgumentException("no structizer found for struct[%s] -> %s".formatted(o, c)));
	}

	private static <T> T useStructizer(Function<? super Structizer, Optional<T>> op, Supplier<? extends IllegalArgumentException> exceptionGenerator) {
		return STRUCTIZERS.get().stream()
				// avoid cyclic loops of providers loading themselves
				.filter(structizer -> !RUNNING_STRUCTIZERS.get().contains(structizer))
				.map(structizer -> {
					RUNNING_STRUCTIZERS.get().add(structizer);
					Optional<T> result = op.apply(structizer);
					RUNNING_STRUCTIZERS.get().remove(structizer);

					return result;
				})
				.flatMap(Optional::stream)
				.findFirst()
				.orElseThrow(exceptionGenerator);
	}
}
