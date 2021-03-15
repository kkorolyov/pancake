package dev.kkorolyov.pancake.platform;

import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.entity.Signature;
import dev.kkorolyov.pancake.platform.utility.Limiter;

/**
 * Performs work on entities matching a certain component signature.
 */
public abstract class GameSystem {
	private final Signature signature;
	private final Limiter limiter;

	protected SharedResources resources;

	/**
	 * Constructs a new system.
	 * @param signature defines all components an entity must have to be affected by this system
	 * @param limiter determines frequency of updates of this system
	 */
	protected GameSystem(Signature signature, Limiter limiter) {
		this.signature = signature;
		this.limiter = limiter;
	}

	/**
	 * Function invoked on each entity affected by this system.
	 * @param entity entity to update
	 * @param dt {@code ns} elapsed since last {@code update} to this system
	 */
	public abstract void update(Entity entity, long dt);

	/**
	 * Function invoked at the beginning of an update cycle.
	 * Intended for any static pre-update logic.
	 * @param dt {@code ns} elapsed since last update to this system
	 */
	public void before(long dt) {}
	/**
	 * Function invoked at the end of an update cycle.
	 * Intended for any static post-update logic.
	 * @param dt {@code ns} elapsed since last update to this system
	 */
	public void after(long dt) {}

	/**
	 * Invoked when this system is attached to a {@link GameEngine}.
	 * Intended for one-time initialization logic, such as registering event receivers.
	 */
	public void attach() {}
	/**
	 * Invoked when this system is detached from a {@link GameEngine}.
	 * Intended for one-time teardown logic, such as clearing event receivers.
	 */
	public void detach() {}

	/** @return system required component signature */
	public Signature getSignature() {
		return signature;
	}
	/** @return system update limiter */
	public Limiter getLimiter() {
		return limiter;
	}

	/**
	 * @param resources shared by {@link GameEngine} this system is attached to
	 */
	void setResources(SharedResources resources) {
		this.resources = resources;
	}
}
