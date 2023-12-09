package dev.kkorolyov.pancake.platform.registry.internal;

import dev.kkorolyov.flub.function.convert.Converter;
import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.entity.EntityTemplate;
import dev.kkorolyov.pancake.platform.io.Structizers;
import dev.kkorolyov.pancake.platform.registry.Resource;
import dev.kkorolyov.pancake.platform.registry.ResourceConverterFactory;
import io.github.classgraph.ClassGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * {@link ResourceConverterFactory} for basic entity templates.
 */
public final class EntityTemplateResourceConverterFactory implements ResourceConverterFactory<EntityTemplate> {
	private static final ThreadLocal<Map<String, List<Class<Component>>>> NAMED_COMPONENTS = ThreadLocal.withInitial(() -> {
		try (var graph = new ClassGraph().enableClassInfo().scan()) {
			var result = new HashMap<String, List<Class<Component>>>();

			for (Class<Component> c : graph.getClassesImplementing(Component.class)
					.filter(t -> !t.isAbstract() && !t.isInterface())
					.loadClasses(Component.class)
			) {
				result.computeIfAbsent(c.getName().toLowerCase(Locale.ROOT), k -> new ArrayList<>()).add(c);
				result.computeIfAbsent(c.getSimpleName().toLowerCase(Locale.ROOT), k -> new ArrayList<>()).add(c);
			}

			return result;
		}
	});

	@Override
	public Converter<Object, Optional<Resource<EntityTemplate>>> get() {
		return Converter.selective(
				t -> t instanceof Map,
				t -> {
					Map<String, List<Class<Component>>> namedComponents = NAMED_COMPONENTS.get();

					return Resource.constant(new EntityTemplate(
							((Map<String, Object>) t).entrySet().stream()
									.map(e -> {
										var matchedClasses = namedComponents.get(e.getKey().toLowerCase(Locale.ROOT));
										if (matchedClasses == null || matchedClasses.isEmpty()) throw new NoSuchElementException("no component class matches [%s]".formatted(e.getKey()));
										else if (matchedClasses.size() > 1) throw new IllegalArgumentException("multiple component class matches for [%s]: %s".formatted(e.getKey(), matchedClasses));
										else {
											var c = matchedClasses.get(0);
											return ((Supplier<Component>) () -> Structizers.fromStruct(c, e.getValue()));
										}
									})
									.toList()
					));
				}
		);
	}

	@Override
	public Class<EntityTemplate> getType() {
		return EntityTemplate.class;
	}
}
