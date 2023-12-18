package dev.kkorolyov.pancake.platform.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

/**
 * Applies a set of components to an entity.
 * Provides static methods for converting between templates/entities and serializable map representations.
 */
public final class EntityTemplate {
	private final Collection<Supplier<Component>> components = new ArrayList<>();

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
