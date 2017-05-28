package dev.kkorolyov.pancake.engine;

import dev.kkorolyov.pancake.component.Signature;

/**
 * Performs work on entities matching a certain component signature.
 */
public abstract class GameSystem {
	private final Signature signature;
	private EntityManager entities;

	/**
	 * Constructs a new system.
	 * @param signature signature defining all components an entity must have to be affected by this system
	 */
	public GameSystem(Signature signature) {
		this.signature = signature;
	}

	/**
	 * Function invoked on all entities affected by this system.
	 * @param dt seconds elapsed since last update
	 */
	public abstract void update(Entity entity, float dt);

	/** @return component signature */
	public Signature getSignature() {
		return signature;
	}

	/** @return all entities known to this system */
	public EntityManager getEntities() {
		return entities;
	}
	/** @param entities all entities known to this system */
	void setEntities(EntityManager entities) {
		this.entities = entities;
	}
}
