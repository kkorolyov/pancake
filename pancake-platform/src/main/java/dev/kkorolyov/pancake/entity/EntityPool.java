package dev.kkorolyov.pancake.entity;

import java.util.*;
import java.util.stream.Stream;

import dev.kkorolyov.pancake.event.EventBroadcaster;

/**
 * A set of uniquely-identified "component-bag" entities.
 */
public class EntityPool {
	private int idCounter = 0;	// Main ID counter
	private Queue<Integer> reclaimedIds = new ArrayDeque<>();	// Reclaimed IDs from destroyed entities
	private final Map<Integer, Entity> entities = new HashMap<>();
	private final EventBroadcaster events;

	/**
	 * Constructs an empty entity pool.
	 * @param events registered event broadcaster
	 */
	public EntityPool(EventBroadcaster events) {
		this.events = events;

		this.events.register("CREATE", (e, c) -> create(c));
		this.events.register("DESTROY", (e, c) -> destroy(e));
	}

	/**
	 * Returns a stream over all entities with a signature subset matching {@code signature}.
	 * @param signature signature defining a set of component types
	 * @param comparator comparator defining entity order, {@code null} results in no sorting
	 * @return all entities with a signature subset matching {@code signature}
	 */
	public Stream<Entity> get(Signature signature, Comparator<Entity> comparator) {
		Stream<Entity> result = entities.values().parallelStream()
																		.filter(entity -> entity.contains(signature));

		if (comparator != null) result = result.sorted(comparator);

		return result;
	}

	/**
	 * Constructs a new entity from a collection of components and adds it to the entity pool.
	 * @param components components composing entity
	 * @return created entity
	 */
	public Entity create(Component... components) {
		return create(Arrays.asList(components));
	}
	/**
	 * Constructs a new entity from a collection of components and adds it to the entity pool.
	 * @param components components composing entity
	 * @return created entity
	 */
	public Entity create(Iterable<Component> components) {
		int id = reclaimedIds.isEmpty() ? idCounter++ : reclaimedIds.remove();
		Entity entity = new Entity(id, components);
		entities.put(id, entity);

		events.enqueue("CREATED", entity, components);
		return entity;
	}

	/**
	 * Removes an entity from the entity pool.
	 * @param entity entity to destroy
	 * @return {@code true} if the entity pool contained {@code entity} and it was removed
	 */
	public boolean destroy(Entity entity) {
		Entity removed = entities.remove(entity.getId());
		boolean result = removed != null;

		if (result) {
			reclaimedIds.add(removed.getId());
			events.enqueue("DESTROYED", removed, null);
		}
		return result;
	}
}
