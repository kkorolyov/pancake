package dev.kkorolyov.pancake.platform.entity;

import dev.kkorolyov.pancake.platform.io.Structizers;
import io.github.classgraph.ClassGraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Applies a set of components to an entity.
 * Provides static methods for converting between templates/entities and serializable map representations.
 */
public final class EntityTemplate {
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
	private final Collection<Supplier<Component>> components = new ArrayList<>();

	/**
	 * Returns a new template with components read from serializable map {@code data}.
	 */
	public static EntityTemplate read(Map<String, Object> data) {
		return new EntityTemplate(
				data.entrySet().stream()
						.map(e -> {
							var matchedClasses = NAMED_COMPONENTS.get().get(e.getKey().toLowerCase(Locale.ROOT));
							if (matchedClasses == null || matchedClasses.isEmpty()) throw new NoSuchElementException("no component class matches [%s]".formatted(e.getKey()));
							else if (matchedClasses.size() > 1) throw new IllegalArgumentException("multiple component class matches for [%s]: %s".formatted(e.getKey(), matchedClasses));
							else {
								var c = matchedClasses.get(0);
								return ((Supplier<Component>) () -> Structizers.fromStruct(c, e.getValue()));
							}
						})
						.toList()
		);
	}
	/**
	 * Returns a serializable map representation of {@code entity} as a template.
	 */
	public static Map<String, Object> write(Entity entity) {
		return StreamSupport.stream(entity.spliterator(), false)
				.collect(Collectors.toMap(
						t -> t.getClass().getName(),
						Structizers::toStruct
				));
	}

	public EntityTemplate(Iterable<? extends Supplier<Component>> components) {
		components.forEach(this.components::add);
	}

	/**
	 * Returns a new template combining all of this template's setup with {@code other}'s.
	 */
	public EntityTemplate and(EntityTemplate other) {
		var result = new EntityTemplate(components);
		result.components.addAll(other.components);
		return result;
	}

	/**
	 * Sets this template's components on {@code entity}.
	 */
	public void apply(Entity entity) {
		for (Supplier<Component> component : components) entity.put(component.get());
	}
}
