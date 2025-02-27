package dev.kkorolyov.pancake.platform.entity;

import dev.kkorolyov.flub.data.SparseMultiset;
import dev.kkorolyov.pancake.platform.Debuggable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * A container of {@link Component}s of distinct implementation types.
 */
public final class Entity implements Iterable<Component>, Debuggable {
	private final int id;
	private final Map<Class<? extends Component>, Component> components = new HashMap<>();
	private final SparseMultiset<Entity, ? super Class<? extends Component>> pool;

	private String debugNameOverride;

	/**
	 * Constructs a new entity attached to {@code pool}.
	 */
	Entity(SparseMultiset<Entity, ? super Class<? extends Component>> pool) {
		this.pool = pool;
		id = this.pool.add(this);
	}

	/**
	 * @see #put(Iterable)
	 */
	public void put(Component... components) {
		put(Arrays.asList(components));
	}
	/**
	 * Adds {@code components} to this entity.
	 * For each component, replaces any existing component of the same type.
	 */
	public void put(Iterable<? extends Component> components) {
		for (Component component : components) {
			this.components.put(component.getClass(), component);
		}
		pool.put(id, this.components.keySet());
	}

	/**
	 * Removes all components from this entity matching {@code types}.
	 */
	@SafeVarargs
	public final void remove(Class<? extends Component>... types) {
		pool.remove(id, components.keySet());
		for (Class<? extends Component> type : types) {
			components.remove(type);
		}
		pool.put(id, components.keySet());
	}

	/**
	 * @param c class of component to get
	 * @param <T> component type
	 * @return the {@code c} component of this entity, or {@code null} if no such component
	 */
	public <T extends Component> T get(Class<T> c) {
		return (T) components.get(c);
	}

	/** @return unique ID of this entity */
	public int getId() {
		return id;
	}

	/**
	 * Returns the number of components in this entity.
	 */
	public int size() {
		return components.size();
	}

	/**
	 * Returns an iterator over this entity's components.
	 */
	@Override
	public Iterator<Component> iterator() {
		return components.values().iterator();
	}

	/**
	 * Returns this entity's debug name.
	 * If it has been explicitly set with {@link #setDebugNameOverride(String)}, returns the current value - which is the same as {@link #getDebugNameOverride()}.
	 * Else returns a name generated from the entity's ID.
	 */
	@Override
	public String getDebugName() {
		return debugNameOverride != null ? debugNameOverride : "Entity " + id;
	}

	/**
	 * Returns the explicitly set debug name, if any.
	 * If this is non-{@code null}, this is the same value as {@link #getDebugName()}.
	 */
	public String getDebugNameOverride() {
		return debugNameOverride;
	}
	/**
	 * Explicitly sets debug name to {@code debugNameOverride}.
	 * A {@code null} value clears the override.
	 */
	public void setDebugNameOverride(String debugNameOverride) {
		this.debugNameOverride = debugNameOverride;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Entity other)) return false;
		return id == other.id;
	}
	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "Entity{" +
				"id=" + id +
				"debugName=" + debugNameOverride +
				", components=" + components +
				'}';
	}
}
