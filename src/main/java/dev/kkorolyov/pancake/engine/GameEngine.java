package dev.kkorolyov.pancake.engine;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Central game management module.
 * Serves as the link between entities with components containing data and systems specifying business logic.
 */
public class GameEngine {
	private final EntityPool entities = new EntityPool();
	private final Set<GameSystem> systems = new LinkedHashSet<>();
	
	/**
	 * Constructs a new engine pre-populated with systems.
	 */
	public GameEngine(GameSystem... systems) {
		for (GameSystem system : systems) add(system);
	}
	/**
	 * Constructs a new engine pre-populated with systems.
	 */
	public GameEngine(Iterable<GameSystem> systems) {
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
