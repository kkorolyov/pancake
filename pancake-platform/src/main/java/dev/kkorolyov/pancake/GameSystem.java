package dev.kkorolyov.pancake;

/**
 * Performs work on entities matching a certain component signature.
 */
public abstract class GameSystem {
	private final Signature signature;
	private EntityPool entities;

	/**
	 * Constructs a new system.
	 * @param signature signature defining all components an entity must have to be affected by this system
	 */
	public GameSystem(Signature signature) {
		this.signature = signature;
	}

	/**
	 * Function invoked on each entity affected by this system.
	 * @param dt seconds elapsed since last update
	 */
	public abstract void update(Entity entity, float dt);

	/**
	 * Function invoked at the beginning of an update cycle.
	 * @param dt seconds elapsed since last update
	 */
	public void before(float dt) {}
	/**
	 * Function invoked at the end of an update cycle.
	 * @param dt seconds elapsed since last update
	 */
	public void after(float dt) {}

	/** @return component signature */
	public Signature getSignature() {
		return signature;
	}

	/** @return all entities known to this system */
	public EntityPool getEntities() {
		return entities;
	}
	/** @param entities all entities known to this system */
	void setEntities(EntityPool entities) {
		this.entities = entities;
	}
}
