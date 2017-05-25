package dev.kkorolyov.pancake;

import java.util.*;
import java.util.Map.Entry;

/**
 * Maintains collections of components which define entity abstractions.
 */
public class EntityManager {
	private int idCounter = 0;	// Main ID counter
	private Queue<Integer> reclaimedIds = new ArrayDeque<>();	// Reclaimed IDs from destroyed entities
	private final Map<Class<? extends Component>, Map<Integer, Component>> componentPool = new HashMap<>();
	
	/**
	 * Constructs a new entity from a collection of components.
	 * Components are added in the order they are passed as parameters, so if multiple components of the same type are passed in, only the latest such component is retained by the final entity.
	 * @param components components composing entity
	 * @return ID of created entity
	 */
	public int create(Component... components) {
		int entityId = getId();
		add(entityId, components);
		
		return entityId;
	}
	private int getId() {
		if (reclaimedIds.isEmpty())
			return idCounter++;
		else
			return reclaimedIds.remove();
	}
	
	/**
	 * Destroys an entity by removing all its components.
	 * @param entityId ID designating entity to destroy
	 * @return {@code true} if at least 1 component bound to {@code entityId} was removed
	 */
	public boolean destroy(int entityId) {
		boolean result = false;
		for (Map<Integer, Component> map : componentPool.values()) {
			if (map.remove(entityId) != null) {
				result = true;
				reclaimedIds.add(entityId);	// Valid ID to reclaim
			}
		}
		return result;
	}
	
	/**
	 * Retrieves a component from an entity.
	 * @param entityId ID designating entity to retrieve from
	 * @param type type of component to retrieve
	 * @return appropriate component, or {@code null} if no such component
	 */
	public <T extends Component> T get(int entityId, Class<T> type) {
		Component result = getMap(type).get(entityId);
		return result == null ? null : type.cast(result);
	}
	/**
	 * Retrieves all components of a particular type.
	 * @param type type of components to retrieve
	 * @return all components of type {@code type}
	 */
	public Set<Entry<Integer, Component>> getAll(Class<? extends Component> type) {
		return getMap(type).entrySet();
	}
	
	/**
	 * Adds components to an entity matching a particular ID.
	 * If there is no such entity, this method is ignored.
	 * If the entity already contains components of types matching the types of new components, those components are replaced.
	 * @param entityId ID designating entity
	 * @param components components to add or replace with
	 */
	public void add(int entityId, Component... components) {
		if (entityId > idCounter)
			return;
		
		for (Component component : components) {
			getMap(component.getClass()).put(entityId, component);
		}
	}
	/**
	 * Removes a component from an entity.
	 * @param entityId ID designating entity to remove from
	 * @param type type of component to remove
	 * @return removed component, or {@code null} if no such component
	 */
	public <T extends Component> T remove(int entityId, Class<T> type) {
		Component result = getMap(type).remove(entityId);
		return result == null ? null : type.cast(result);
	}
	
	private Map<Integer, Component> getMap(Class<? extends Component> type) {
		Map<Integer, Component> map = componentPool.get(type);
		
		if (map == null) {
			map = new HashMap<Integer, Component>();
			componentPool.put(type, map);
		}
		return map;
	}
}
