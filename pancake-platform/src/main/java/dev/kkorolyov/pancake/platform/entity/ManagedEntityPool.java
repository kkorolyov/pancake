package dev.kkorolyov.pancake.platform.entity;

import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.event.EventBroadcaster;

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
import java.util.function.Function;
import java.util.stream.Stream;

import static dev.kkorolyov.pancake.platform.event.Events.CREATE;
import static dev.kkorolyov.pancake.platform.event.Events.CREATED;
import static dev.kkorolyov.pancake.platform.event.Events.DESTROY;
import static dev.kkorolyov.pancake.platform.event.Events.DESTROYED;

/**
 * An {@link EntityPool} implementation with exposed management methods.
 */
public class ManagedEntityPool implements EntityPool {
	private final Map<Class<? extends Component>, Map<UUID, Component>> components = new HashMap<>();
	private final Map<UUID, Collection<Action>> actions = new LinkedHashMap<>();
	private final EventBroadcaster events;

	/**
	 * Constructs an empty entity pool.
	 * @param events registered event broadcaster
	 */
	public ManagedEntityPool(EventBroadcaster events) {
		this.events = events;

		this.events.register(CREATE, (Consumer<Iterable<Component>>) this::create);
		this.events.register(DESTROY, (Consumer<UUID>) this::destroy);
	}

	@Override
	public boolean contains(UUID id, Signature signature) {
		return signature.getTypes().stream()
				.allMatch(type -> get(id, type) != null);
	}

	@Override
	public Stream<Component> get(UUID id) {
		return components.values().stream()
				.map(componentMap -> componentMap.get(id))
				.filter(Objects::nonNull);
	}

	@Override
	public <T extends Component> T get(UUID id, Class<T> type) {
		return type.cast(getComponentMap(type).get(id));
	}
	@Override
	public <T extends Component, R> R get(UUID id, Class<T> type, Function<T, R> function) {
		T component = get(id, type);

		return component != null
				? function.apply(component)
				: null;
	}

	@Override
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

	@Override
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
