package dev.kkorolyov.pancake.platform.entity;

import dev.kkorolyov.pancake.platform.io.Structizers;

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
