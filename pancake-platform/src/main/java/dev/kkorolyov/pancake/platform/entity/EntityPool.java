package dev.kkorolyov.pancake.platform.entity;

import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.event.EventBroadcaster;
import dev.kkorolyov.pancake.platform.event.Events;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static dev.kkorolyov.pancake.platform.event.Events.CREATE;
import static dev.kkorolyov.pancake.platform.event.Events.CREATED;
import static dev.kkorolyov.pancake.platform.event.Events.DESTROY;
import static dev.kkorolyov.pancake.platform.event.Events.DESTROYED;

/**
 * A set of uniquely-identified "component-bag" entities.
 * <pre>
 * Events received:
 * {@link Events#CREATE} - creates an entity (Entity)
 * {@link Events#DESTROY} - destroys an entity (Entity)
 *
 * Events emitted:
 * {@link Events#CREATED} - when an entity is created (Entity)
 * {@link Events#DESTROYED} - when an entity is destroyed (Entity)
 * </pre>
 */
public class EntityPool {
	private final Map<Class<? extends Component>, Map<UUID, Component>> components = new HashMap<>();
	private final Map<UUID, Collection<Action>> actions = new LinkedHashMap<>();
	private final EventBroadcaster events;

	/**
	 * Constructs an empty entity pool.
	 * @param events registered event broadcaster
	 */
	public EntityPool(EventBroadcaster events) {
		this.events = events;

		this.events.register(CREATE, (Consumer<Iterable<Component>>) this::create);
		this.events.register(DESTROY, (Consumer<UUID>) this::destroy);
	}

	/** @return whether entity with ID {@code id} masks {@code signature} */
	public boolean contains(UUID id, Signature signature) {
		return signature.getTypes().stream()
				.allMatch(type -> get(id, type) != null);
	}

	/**
	 * @param id entity ID
	 * @return over all components of entity with ID {@code id}
	 */
	public Stream<Component> get(UUID id) {
		return components.values().stream()
				.map(componentMap -> componentMap.get(id))
				.filter(Objects::nonNull);
	}
	/**
	 * Retrieves a component for a particular entity.
	 * @param id entity ID
	 * @param type type of component
	 * @return component of type {@code type} for entity with ID {@code id}, or {@code null} if does not exist
	 */
	public <T extends Component> T get(UUID id, Class<T> type) {
		return type.cast(getComponentMap(type).get(id));
	}

	/**
	 * Returns a stream over all entities masking a signature.
	 * @param signature signature defining a set of component types
	 * @param comparator comparator defining entity order, {@code null} results in no sorting
	 * @return all entities with a signature subset matching {@code signature}
	 */
	public Stream<UUID> get(Signature signature, Comparator<UUID> comparator) {
		Stream<UUID> result;

		switch(signature.size()) {
			case 0:
				result = Stream.empty();
				break;
			case 1:
				result = getComponentMap(signature.getTypes().iterator().next()).keySet().stream();
				break;
			default:
				result = signature.getTypes().stream()
						.reduce(new HashSet<>(getComponentMap(signature.getTypes().iterator().next()).keySet()), (tempResult, type) -> {
							tempResult.removeIf(id -> !getComponentMap(type).containsKey(id));
							return tempResult;
						}, (set1, set2) -> set1)  // Will only ever be 1 set
						.stream();
				break;
		}
		if (comparator != null) result = result.sorted(comparator);

		return result;
	}

	/**
	 * @see #create(Iterable)
	 */
	public UUID create(Component... components) {
		return create(Arrays.asList(components));
	}
	/**
	 * Adds an entity to the pool.
	 * @param components components defining entity, if this collection contains multiple instances of the same component type, the last-encountered overrides the rest
	 * @return ID assigned to entity
	 */
	public UUID create(Iterable<Component> components) {
		UUID id = UUID.randomUUID();

		components.forEach(component -> add(id, component));
		events.enqueue(CREATED, id);

		return id;
	}
	/**
	 * Removes an entity from the pool.
	 * @param id ID of entity to remove
	 * @return number of components which were bound to {@code id} and removed
	 */
	public long destroy(UUID id) {
		long count = components.values().stream()
				.map(componentMap -> componentMap.remove(id))
				.filter(Objects::nonNull)
				.count();

		if (count > 0) events.enqueue(DESTROYED, id);

		return count;
	}

	/**
	 * Adds or replaces a component on an entity.
	 * Replaces the entity's current component of the same type bound if it exists.
	 * @param id ID of entity to alter
	 * @param component component to set
	 */
	public void add(UUID id, Component component) {
		getComponentMap(component.getClass()).put(id, component);
	}
	/**
	 * Removes a component from an entity.
	 * @param id ID of entity to alter
	 * @param type type of component to remove
	 */
	public void remove(UUID id, Class<? extends Component> type) {
		getComponentMap(type).remove(id);
	}


	private Map<UUID, Component> getComponentMap(Class<? extends Component> type) {
		return components.computeIfAbsent(type, k -> new HashMap<>());
	}

	/**
	 * Adds an action to an entity to be applied at the beginning of the next tick.
	 * @param id ID of entity to add action to
	 * @param action action to add
	 */
	public void add(UUID id, Action action) {
		actions.computeIfAbsent(id, k -> new HashSet<>())
				.add(action);
	}

	/**
	 * Applies actions of all entities.
	 */
	public void applyActions() {
		actions.forEach((id, actions) ->
				actions.forEach(action -> action.accept(id, this)));
	}
}
