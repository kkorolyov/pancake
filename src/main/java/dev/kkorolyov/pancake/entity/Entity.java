package dev.kkorolyov.pancake.entity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import dev.kkorolyov.pancake.Component;

/**
 * A single entity found in the game world. Consists of a unique ID and a set of distinctly-typed {@code Component} objects.
 */
public final class Entity implements Iterable<Component> {
	//private final int id;	 TODO implement
	private final Set<Component> components = new HashSet<>();
	
	/**
	 * Constructs a new entity composed of a set of components.
	 * @param components initial components defining this entity
	 */
	public Entity(Component... components) {
		for (Component component : components)
			add(component);
	}
	
	/**
	 * Checks if this entity has all components of particular types.
	 * @param types component types
	 * @return {@code true} if this entity has all such components
	 */
	@SafeVarargs
	public final boolean contains(Class<? extends Component>... types) {
		for (Class<? extends Component> type : types) {
			if (get(type) == null)
				return false;
		}
		return true;
	}
	
	/**
	 * Returns a component of a particular type.
	 * @param type component type
	 * @return appropriate component, or {@code null} if this entity has no such component
	 */
	public <T extends Component> T get(Class<T> type) {
		for (Component component : components) {
			if (component.getClass() == type)
				return type.cast(component);
		}
		return null;
	}
	
	/**
	 * Adds a component to this entity.
	 * If this entity already contains a component of the same type, it is overwritten with this component.
	 * @param component component to add
	 * @return {@code true} if adding this component overwrote another component of the same type
	 */
	public boolean add(Component component) {
		boolean overwrite = remove(component.getClass());
		
		components.add(component);
		return overwrite;
	}
	/**
	 * Removes a component by type.
	 * @param type component type
	 * @return {@code true} if this entity had such a component and it was removed
	 */
	public <T extends Component> boolean remove(Class<T> type) {
		Iterator<Component> it = iterator();
		
		while (it.hasNext()) {
			if (it.next().getClass() == type) {
				it.remove();
				return true;
			}
		}
		return false;
	}
	
	@Override
	public Iterator<Component> iterator() {
		return components.iterator();
	}
}
