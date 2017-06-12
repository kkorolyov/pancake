package dev.kkorolyov.pancake.entity;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import dev.kkorolyov.pancake.event.EventBroadcaster;
import dev.kkorolyov.pancake.event.Events;

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
	}

	/**
	 * Returns all entities with a signature subset matching {@code signature}.
	 * @param signature signature defining a set of component types
	 * @return all entities with a signature subset matching {@code signature}
	 */
	public Iterable<Entity> get(Signature signature) {
		return entities.values().parallelStream()
									 .filter(entity -> entity.contains(signature))
									 .collect(Collectors.toSet());
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

		events.enqueue(Events.CREATED, entity, null);
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
			events.enqueue(Events.DESTROYED, entity, null);
		}
		return result;
	}
}
