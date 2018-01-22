package dev.kkorolyov.pancake.platform.entity;

import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.event.CreateEntity;
import dev.kkorolyov.pancake.platform.event.DestroyEntity;
import dev.kkorolyov.pancake.platform.event.EntityCreated;
import dev.kkorolyov.pancake.platform.event.EntityDestroyed;
import dev.kkorolyov.pancake.platform.event.management.EventBroadcaster;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * An {@link EntityPool} implementation with exposed management methods.
 */
public class ManagedEntityPool implements EntityPool {
	private final Map<Class<? extends Component>, Map<Integer, Component>> components = new HashMap<>();
	private final Map<Integer, Collection<Action>> actions = new LinkedHashMap<>();
	private final EventBroadcaster events;

	private int counter = 0;
	private final Queue<Integer> reclaimedIds = new ArrayDeque<>();

	private final Set<Integer> bySignatureTempResult = new HashSet<>();

	/**
	 * Constructs an empty entity pool.
	 * @param events registered event broadcaster
	 */
	public ManagedEntityPool(EventBroadcaster events) {
		this.events = events;

		this.events.register(CreateEntity.class, e -> create(e.getComponents()));
		this.events.register(DestroyEntity.class, e -> destroy(e.getId()));
	}

	@Override
	public boolean contains(int id, Signature signature) {
		return signature.getTypes().stream()
				.allMatch(type -> get(id, type) != null);
	}

	@Override
	public Stream<Component> get(int id) {
		return components.values().stream()
				.map(componentMap -> componentMap.get(id))
				.filter(Objects::nonNull);
	}

	@Override
	public <T extends Component> T get(int id, Class<T> type) {
		return type.cast(getComponentMap(type).get(id));
	}
	@Override
	public <T extends Component, R> R get(int id, Class<T> type, Function<T, R> function) {
		T component = get(id, type);

		return component != null
				? function.apply(component)
				: null;
	}

	/**
	 * Invokes an action on all entities masking a signature.
	 * @param signature signature defining a set of component types
	 * @param action action to invoke on each matching entity
	 */
	public void forEachMatching(Signature signature, Consumer<Integer> action) {
		Iterator<Class<? extends Component>> it = signature.getTypes().iterator();

		bySignatureTempResult.clear();
		bySignatureTempResult.addAll(getComponentMap(it.next()).keySet());
		while (it.hasNext()) bySignatureTempResult.retainAll(getComponentMap(it.next()).keySet());

		bySignatureTempResult.forEach(action);
	}

	/**
	 * @see #create(Iterable)
	 */
	public int create(Component... components) {
		return create(Arrays.asList(components));
	}
	/**
	 * Adds an entity to the pool.
	 * @param components components defining entity, if this collection contains multiple instances of the same component type, the last-encountered overrides the rest
	 * @return ID assigned to entity
	 */
	public int create(Iterable<Component> components) {
		int id = reclaimedIds.isEmpty()
				? counter++
				: reclaimedIds.remove();

		components.forEach(component -> add(id, component));
		events.enqueue(new EntityCreated(id));

		return id;
	}
	/**
	 * Removes an entity from the pool.
	 * @param id ID of entity to remove
	 * @return number of components which were bound to {@code id} and removed
	 */
	public long destroy(int id) {
		long count = components.values().stream()
				.map(componentMap -> componentMap.remove(id))
				.filter(Objects::nonNull)
				.count();

		if (count > 0) {
			events.enqueue(new EntityDestroyed(id));
			reclaimedIds.add(id);
		}
		return count;
	}

	/**
	 * Adds or replaces a component on an entity.
	 * Replaces the entity's current component of the same type if it exists.
	 * @param id ID of entity to alter
	 * @param component component to set
	 */
	public void add(int id, Component component) {
		components.computeIfAbsent(component.getClass(), k -> new HashMap<>()).put(id, component);
	}
	/**
	 * Removes a component from an entity.
	 * @param id ID of entity to alter
	 * @param type type of component to remove
	 */
	public void remove(int id, Class<? extends Component> type) {
		getComponentMap(type).remove(id);
	}

	private Map<Integer, Component> getComponentMap(Class<? extends Component> type) {
		return components.getOrDefault(type, Collections.emptyMap());
	}

	@Override
	public void add(int id, Action action) {
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
