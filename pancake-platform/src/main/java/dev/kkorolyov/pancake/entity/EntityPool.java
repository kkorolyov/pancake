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

		events.register("DESTROY", e -> destroy(e.getId()));
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
		int id = reclaimedIds.isEmpty() ? idCounter++ : reclaimedIds.remove();
		Entity entity = new Entity(id, components);
		entities.put(id, entity);

		events.enqueue("CREATED", entity);
		return entity;
	}
	/**
	 * Removes an entity from the entity pool.
	 * @param id ID designating entity to destroy
	 * @return {@code true} if the entity pool contained an entity with ID matching {@code id}
	 */
	public boolean destroy(int id) {
		Entity entity = entities.remove(id);
		boolean result = entity != null;

		if (result) {
			reclaimedIds.add(id);
			events.enqueue("DESTROYED", entity);
		}
		return result;
	}
}
