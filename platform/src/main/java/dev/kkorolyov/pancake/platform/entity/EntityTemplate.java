package dev.kkorolyov.pancake.platform.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Applies a set of components to an entity.
 * Provides static methods for converting between templates/entities and serializable map representations.
 */
public final class EntityTemplate {
	private final Collection<Supplier<Component>> components = new ArrayList<>();

	/**
	 * Returns a new template with components read from serializable map {@code data}.
	 */
	public static EntityTemplate read(Map<String, Object> data) {
		return new EntityTemplate(
				data.entrySet().stream()
						.map(e -> {
							ComponentConverter<Component> converter = ComponentConverters.get(e.getKey());
							return ((Supplier<Component>) () -> converter.read(e.getValue()));
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
						t -> ComponentConverters.get(t.getClass().getName()).write(t)
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
