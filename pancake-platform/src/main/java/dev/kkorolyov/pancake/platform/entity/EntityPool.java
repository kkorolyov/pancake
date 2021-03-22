package dev.kkorolyov.pancake.platform.entity;

import dev.kkorolyov.flopple.data.FacetedBundle;
import dev.kkorolyov.pancake.platform.event.CreateEntity;
import dev.kkorolyov.pancake.platform.event.DestroyEntity;
import dev.kkorolyov.pancake.platform.event.EntityCreated;
import dev.kkorolyov.pancake.platform.event.EntityDestroyed;
import dev.kkorolyov.pancake.platform.event.EventBroadcaster;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Stream;

import static dev.kkorolyov.flopple.collections.Iterables.append;

/**
 * A set of uniquely-identified "component-bag" entities.
 */
public class EntityPool {
	private final FacetedBundle<Integer, Class<? extends Component>, ManagedEntity> entities = new FacetedBundle<>();
	private final EventBroadcaster events;

	private int counter;
	private final Queue<Integer> reclaimedIds = new ArrayDeque<>();

	/**
	 * Constructs an empty entity pool.
	 * @param events registered event broadcaster
	 */
	public EntityPool(EventBroadcaster events) {
		this.events = events;

		this.events.register(CreateEntity.class, e -> create().add(e.getComponents()));
		this.events.register(DestroyEntity.class, e -> destroy(e.getId()));
	}

	/**
	 * @param id ID of entity to get
	 * @return entity with ID {@code id}, or {@code null} if no such entity
	 */
	public ManagedEntity get(int id) {
		FacetedBundle.Entry<Class<? extends Component>, ManagedEntity> entry = entities.get(id);
		return entry != null ? entry.getElement() : null;
	}

	/**
	 * @param signature signature to match
	 * @return stream over all entities in this pool masking {@code signature}
	 */
	public Stream<ManagedEntity> stream(Signature signature) {
		return entities.stream(signature.getTypes());
	}

	/**
	 * Creates a new, empty entity attached to this pool.
	 * @return created entity
	 */
	public ManagedEntity create() {
		int id = reclaimedIds.isEmpty()
				? counter++
				: reclaimedIds.remove();

		ManagedEntity entity = new ManagedEntity(id);

		entities.put(entity.getId(), entity);
		events.enqueue(new EntityCreated(id));

		return entity;
	}
	/**
	 * Removes an entity from this pool.
	 * @param id ID of entity to remove
	 */
	public void destroy(int id) {
		if (entities.remove(id)) {
			events.enqueue(new EntityDestroyed(id));
			reclaimedIds.add(id);
		}
	}

	/**
	 * An {@link Entity} implementation attached to its owning {@link EntityPool} and with exposed management methods.
	 */
	public class ManagedEntity implements Entity {
		private final int id;
		private final Map<Class<? extends Component>, Component> components = new HashMap<>();

		private ManagedEntity(int id) {
			this.id = id;
		}

		/** @see #add(Iterable) */
		public void add(Component component, Component... components) {
			add(append(component, components));
		}
		/**
		 * @param components components to add or replace existing components of the same type
		 */
		public void add(Iterable<? extends Component> components) {
			for (Component component : components) {
				this.components.put(component.getClass(), component);

				entities.get(id).addFacets(component.getClass());
			}
		}

		/**
		 * @param componentTypes classes of components to remove
		 */
		public void remove(Iterable<Class<? extends Component>> componentTypes) {
			for (Class<? extends Component> type : componentTypes) {
				components.remove(type);

				entities.get(id).removeFacets(type);
			}
		}

		@Override
		public <T extends Component> T get(Class<T> c) {
			return (T) components.get(c);
		}

		@Override
		public int getId() {
			return id;
		}

		@Override
		public Iterator<Component> iterator() {
			return components.values().iterator();
		}

		@Override
		public int compareTo(Entity o) {
			return Integer.compare(id, o.getId());
		}
	}
}
