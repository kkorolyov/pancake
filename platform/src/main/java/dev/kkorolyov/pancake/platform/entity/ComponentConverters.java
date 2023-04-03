package dev.kkorolyov.pancake.platform.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

/**
 * Provides access to {@link ComponentConverter} implementations on the classpath.
 */
public final class ComponentConverters {
	private static final Logger log = LoggerFactory.getLogger(ComponentConverters.class);

	private static final ThreadLocal<Map<String, List<ComponentConverter<?>>>> converters = ThreadLocal.withInitial(() -> {
				var typeToConverter = ServiceLoader.load(ComponentConverter.class).stream()
						.map(ServiceLoader.Provider::get)
						.collect(Collectors.toUnmodifiableMap(
								ComponentConverter::getType,
								t -> t,
								(provider, other) -> {
									throw new IllegalStateException("multiple providers found for " + provider.getType() + ": [" + provider + ", " + other + "]");
								}
						));

				log.info("loaded {} {} providers: {}", typeToConverter.size(), ComponentConverter.class, typeToConverter);

				Map<String, List<ComponentConverter<?>>> result = new HashMap<>();

				typeToConverter.forEach((type, converter) -> {
					result.computeIfAbsent(type.getName(), k -> new ArrayList<>()).add(converter);
					result.computeIfAbsent(type.getSimpleName(), k -> new ArrayList<>()).add(converter);
				});

				return result;
			}
	);

	private ComponentConverters() {}

	/**
	 * Returns the converter for the component class matching {@code name}.
	 * {@code name} may be a simple name or fully-qualified.
	 * If simple, and the name clashes with another component class simple name for which a converter exists, throws {@link IllegalArgumentException}.
	 * If no matching converter found, throws {@link NoSuchElementException}.
	 */
	public static <T extends Component> ComponentConverter<T> get(String name) {
		var results = converters.get().get(name);
		if (results != null) {
			if (results.size() == 1) {
				return (ComponentConverter<T>) results.get(0);
			} else {
				throw new IllegalArgumentException("multiple providers found for " + name + ": " + results + "; consider using the fully-qualified component name, instead");
			}
		} else {
			throw new NoSuchElementException("no provider found for converting " + name);
		}
	}
}
