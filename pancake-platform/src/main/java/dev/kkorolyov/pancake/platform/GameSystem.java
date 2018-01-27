package dev.kkorolyov.pancake.platform;

import dev.kkorolyov.pancake.platform.entity.EntityPool;
import dev.kkorolyov.pancake.platform.entity.Signature;
import dev.kkorolyov.pancake.platform.event.management.EventBroadcaster;
import dev.kkorolyov.pancake.platform.utility.PerformanceCounter;
import dev.kkorolyov.pancake.platform.utility.PerformanceCounter.Usage;

import java.util.Comparator;
import java.util.UUID;

/**
 * Performs work on entities matching a certain component signature.
 */
public abstract class GameSystem {
	private final Signature signature;
	private final Comparator<UUID> comparator;

	protected EntityPool entities;
	protected EventBroadcaster events;
	private PerformanceCounter performanceCounter;

	/**
	 * Constructs a new system with arbitrary entity order.
	 * @param signature defines all components an entity must have to be affected by this system
	 */
	protected GameSystem(Signature signature) {
		this(signature, null);
	}
	/**
	 * Constructs a new system.
	 * @param signature defines all components an entity must have to be affected by this system
	 * @param comparator defines the order in which entities are supplied to this system
	 */
	protected GameSystem(Signature signature, Comparator<UUID> comparator) {
		this.signature = signature;
		this.comparator = comparator;
	}

	/**
	 * Function invoked on each entity affected by this system.
	 * @param id ID of entity to update
	 * @param dt seconds elapsed since last update
	 */
	public abstract void update(int id, float dt);

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

	/**
	 * Invoked when this system is attached to a {@link GameEngine}.
	 */
	public void attach() {}
	/**
	 * Invoked when this system is detached from a {@link GameEngine}.
	 */
	public void detach() {}

	/**
	 * Used by a {@link GameEngine} to share services.
	 * @param entities shared entity pool
	 * @param events shared event broadcaster
	 * @param performanceCounter shared performance counter
	 */
	void share(EntityPool entities, EventBroadcaster events, PerformanceCounter performanceCounter) {
		this.entities = entities;
		this.events = events;
		this.performanceCounter = performanceCounter;
	}

	/** @return average ticks per second */
	protected long getTps() {
		return performanceCounter.getTps();
	}
	/** @return current performance counter usages */
	protected Iterable<Usage> usages() {
		return performanceCounter.getUsages();
	}

	/** @return component signature */
	public Signature getSignature() {
		return signature;
	}
	/** @return required entity order */
	public Comparator<UUID> getComparator() {
		return comparator;
	}
}
