package dev.kkorolyov.pancake.platform.entity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A single entity found in the game world. Consists of a unique ID and a set of distinctly-typed {@code Component} objects.
 */
public class Entity implements Comparable<Entity> {
	private final int id;
	private final Signature signature = new Signature();
	private final Map<Class<? extends Component>, Component> components = new HashMap<>();
	
	/**
	 * Constructs a new entity composed of a set of components.
	 * @param id entity id
	 * @param components set of components defining this entity
	 */
	public Entity(int id, Component... components) {
		this(id, Arrays.asList(components));
	}
	/**
	 * Constructs a new entity composed of a set of components.
	 * @param id entity id
	 * @param components set of components defining this entity
	 */
	public Entity(int id, Iterable<Component> components) {
		this.id = id;
		components.forEach(this::add);
	}

	/**
	 * Checks if this entity contains a subset of component types defined by {@code signature}
	 * @param signature signature defining set of component types
	 * @return {@code true} if this entity contains all component types defined by {@code signature}
	 */
	public boolean contains(Signature signature) {
		return this.signature.masks(signature);
	}

	/**
	 * Returns the component of a particular type.
	 * @param type component type
	 * @return appropriate component, or {@code null} if this entity contains no such component
	 */
	public <T extends Component> T get(Class<T> type) {
		return type.cast(components.get(type));
	}
	
	/**
	 * Adds a component to this entity.
	 * If this entity already contains a component of the same type, it is overwritten with this component.
	 * @param component component to add
	 * @return {@code true} if adding this component overwrote another component of the same type
	 */
	public boolean add(Component component) {
		boolean overwrite = remove(component.getClass());

		signature.add(component.getClass());
		components.put(component.getClass(), component);

		return overwrite;
	}
	/**
	 * Removes a component by type.
	 * @param type component type
	 * @return {@code true} if this entity had such a component and it was removed
	 */
	public <T extends Component> boolean remove(Class<T> type) {
		signature.remove(type);
		return components.remove(type) != null;
	}

	/**
	 * Checks if this entity's signature matches some other signature.
	 * @param signature signature to match
	 * @return {@code true} if this entity's signature equals {@code signature}
	 */
	public boolean matches(Signature signature) {
		return this.signature.equals(signature);
	}

	/** @return entity ID */
	public int getId() {
		return id;
	}

	/** Checks for equality by ID. */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Entity entity = (Entity) o;

		return id == entity.id;
	}
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	/**	Compares entities by ID. */
	@Override
	public int compareTo(Entity o) {
		return Integer.compare(id, o.id);
	}
}
