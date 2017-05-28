package dev.kkorolyov.pancake.engine;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Maintains a collection of entities.
 * An entity is composed of a unique ID and a set of uniquely-typed {@code Components}.
 */
public class GameEngine {
	private final EntityManager entities = new EntityManager();
	private final Set<GameSystem> systems = new LinkedHashSet<>();
	
	/**
	 * Constructs a new engine pre-populated with systems.
	 */
	public GameEngine(GameSystem... systems) {
		for (GameSystem system : systems) add(system);
	}
	
	/**
	 * Applies a single timestep of system updates on all applicable entities.
	 * @param dt seconds elapsed since last update
	 */
	public void update(float dt) {
		for (GameSystem system : systems) {
			for (Entity entity : entities.get(system.getSignature())) {
				system.update(entity, dt);
			}
		}
	}

	/** @param system added system */
	public void add(GameSystem system) {
		system.setEntities(entities);
		systems.add(system);
	}
	/** @param system removed system */
	public void remove(GameSystem system) {
		systems.remove(system);
	}
}
