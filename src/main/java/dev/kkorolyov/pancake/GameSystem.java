package dev.kkorolyov.pancake;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Performs work on entities matching a certain component signature.
 */
public abstract class GameSystem {
	private final Signature signature;

	/**
	 * Constructs a new system.
	 * @param signature signature defining all components an entity must have to be affected by this system
	 */
	public GameSystem(Signature signature) {
		this.signature = signature;
	}

	/** @return entities on which this system performs work */
	protected EntityManager getEntities() {
		return engine.getEntities();
	}
	
	/**
	 * Updates applicable entities.
	 * @param dt seconds elapsed since last update
	 * @return number of updated entities
	 */
	public abstract int update(Iterable<Entity> entities, float dt);
}
