package dev.kkorolyov.pancake.platform;

import dev.kkorolyov.pancake.platform.entity.EntityPool;
import dev.kkorolyov.pancake.platform.entity.Signature;
import dev.kkorolyov.pancake.platform.event.EventBroadcaster;
import dev.kkorolyov.pancake.platform.utility.PerformanceCounter;
import dev.kkorolyov.pancake.platform.utility.PerformanceCounter.Usage;

import java.util.Comparator;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Performs work on entities matching a certain component signature.
 */
public abstract class GameSystem {
	private final Signature signature;
	private final Comparator<UUID> comparator;

	private EventBroadcaster events;
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
	 * @param dt seconds elapsed since last update
	 */
	public abstract void update(UUID id, EntityPool entities, float dt);

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
	 * @param events shared event broadcaster
	 * @param performanceCounter shared performance counter
	 */
	void share(EventBroadcaster events, PerformanceCounter performanceCounter) {
		this.events = events;
		this.performanceCounter = performanceCounter;
	}

	/**
	 * Registers to receive broadcasts of an event.
	 * @param event event identifier
	 * @param receiver action invoked on event reception
	 */
	public void register(String event, Consumer<?> receiver) {
		events.register(event, receiver);
	}
	/**
	 * Removes a receiver from a set of registered receivers
	 * @param event event identifier
	 * @param receiver removed receiver
	 * @return {@code true} if {@code receiver} was present and removed
	 */
	public boolean unregister(String event, Consumer<?> receiver) {
		return events.unregister(event, receiver);
	}

	/**
	 * Queues an event.
	 * @param event event identifier
	 * @param payload event payload
	 */
	public void enqueue(String event, Object payload) {
		events.enqueue(event, payload);
	}

	/** @return current performance counter usages */
	protected Iterable<Usage> usages() {
		return performanceCounter.usages();
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
