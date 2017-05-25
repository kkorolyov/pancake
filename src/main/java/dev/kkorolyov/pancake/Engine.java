package dev.kkorolyov.pancake;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Maintains a collection of entities.
 * An entity is composed of a unique ID and a set of uniquely-typed {@code Component} objects.
 */
public class Engine {
	//private final Set<Entity> entities = new LinkedHashSet<>();
	private final EntityManager entities = new EntityManager();
	private final Set<GameSystem> systems = new LinkedHashSet<>();
	
	/**
	 * Constructs a new engine pre-populated with systems.
	 */
	public Engine(GameSystem... systems) {
		for (GameSystem system : systems)
			add(system);
	}
	
	/**
	 * Updates all systems.
	 * @param dt seconds elapsed since last update
	 */
	public void update(float dt) {
		for (GameSystem system : systems)
			system.update(dt);
	}
	
	/** @return entities managed by this engine */
	public EntityManager getEntities() {
		return entities;
	}
	
	/** @param system system to add */
	public void add(GameSystem system) {
		system.register(this);
		systems.add(system);
	}
}
