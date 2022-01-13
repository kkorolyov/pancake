package dev.kkorolyov.pancake.platform.entity;

import dev.kkorolyov.flub.data.SparseMultiset;
import dev.kkorolyov.pancake.platform.event.CreateEntity;
import dev.kkorolyov.pancake.platform.event.DestroyEntity;
import dev.kkorolyov.pancake.platform.event.EntityCreated;
import dev.kkorolyov.pancake.platform.event.EntityDestroyed;
import dev.kkorolyov.pancake.platform.event.EventLoop;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import static dev.kkorolyov.flub.collections.Iterables.append;

/**
 * A set of uniquely-identified "component-bag" entities.
 */
public final class EntityPool {
	private final SparseMultiset<ManagedEntity, Class<? extends Component>> entities = new SparseMultiset<>();
	private final EventLoop events;

	/**
	 * Constructs an empty entity pool.
	 * @param events registered event broadcaster
	 */
	public EntityPool(EventLoop events) {
		this.events = events;

		this.events.register(CreateEntity.class, e -> create().put(e.getComponents()));
		this.events.register(DestroyEntity.class, e -> destroy(e.getId()));
	}

	/**
	 * @param id ID of entity to get
	 * @return entity with ID {@code id}, or {@code null} if no such entity
	 */
	public ManagedEntity get(int id) {
		return entities.get(id);
	}
	/**
	 * @param signature signature to match
	 * @return all entities in this pool masking {@code signature}
	 */
	public Iterable<ManagedEntity> get(Iterable<Class<? extends Component>> signature) {
		return entities.get(signature);
	}

	/**
	 * Creates a new, empty entity attached to this pool.
	 * @return created entity
	 */
	public ManagedEntity create() {
		ManagedEntity entity = new ManagedEntity();
		events.enqueue(new EntityCreated(entity.id));

		return entity;
	}
	/**
	 * Removes an entity from this pool.
	 * @param id ID of entity to remove
	 */
	public void destroy(int id) {
		if (entities.remove(id)) events.enqueue(new EntityDestroyed(id));
	}

	@Override
	public String toString() {
		return "EntityPool{" +
				"entities=" + entities +
				", events=" + events +
				'}';
	}

	/**
	 * An {@link Entity} implementation attached to its owning {@link EntityPool} and with exposed management methods.
	 */
	public final class ManagedEntity implements Entity {
		private final int id;
		private final Map<Class<? extends Component>, Component> components = new HashMap<>();

		private ManagedEntity() {
			id = entities.add(this);
		}

		/** @see #put(Iterable) */
		public void put(Component component, Component... components) {
			put(append(component, components));
		}
		/** @param components components to add or replace existing components of the same type */
		public void put(Iterable<? extends Component> components) {
			for (Component component : components) {
				this.components.put(component.getClass(), component);
			}
			entities.put(id, this.components.keySet());
		}

		/** @see #remove(Iterable) */
		public void remove(Class<? extends Component> type, Class<? extends Component>... types) {
			remove(append(type, types));
		}
		/** @param componentTypes classes of components to remove */
		public void remove(Iterable<Class<? extends Component>> componentTypes) {
			entities.remove(id, components.keySet());
			for (Class<? extends Component> type : componentTypes) {
				components.remove(type);
			}
			entities.put(id, components.keySet());
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

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null || getClass() != obj.getClass()) return false;
			ManagedEntity o = (ManagedEntity) obj;
			return id == o.id && Objects.equals(components, o.components);
		}
		@Override
		public int hashCode() {
			return Objects.hash(id, components);
		}

		@Override
		public String toString() {
			return "ManagedEntity{" +
					"id=" + id +
					", components=" + components +
					'}';
		}
	}
}
