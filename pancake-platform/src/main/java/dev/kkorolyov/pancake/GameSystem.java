package dev.kkorolyov.pancake;

import dev.kkorolyov.pancake.entity.Entity;
import dev.kkorolyov.pancake.entity.Signature;
import dev.kkorolyov.pancake.event.EventBroadcaster;
import dev.kkorolyov.pancake.event.Receiver;

import java.util.Comparator;

/**
 * Performs work on entities matching a certain component signature.
 */
public abstract class GameSystem {
	private final Signature signature;
	private final Comparator<Entity> comparator;
	private EventBroadcaster events;

	/**
	 * Constructs a new system with arbitrary entity order.
	 * @param signature defines all components an entity must have to be affected by this system
	 */
	public GameSystem(Signature signature) {
		this(signature, null);
	}
	/**
	 * Constructs a new system.
	 * @param signature defines all components an entity must have to be affected by this system
	 * @param comparator defines the order in which entities are supplied to this system
	 */
	public GameSystem(Signature signature, Comparator<Entity> comparator) {
		this.signature = signature;
		this.comparator = comparator;
	}

	/**
	 * Invoked when this system is attached to a {@link GameEngine}.
	 */
	public void attach() {}
	/**
	 * Invoked when this system is detached from a {@link GameEngine}.
	 */
	public void detach() {}

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

	/**
	 * Registers to receive broadcasts of an event.
	 * @param event event identifier
	 * @param receiver action invoked on event reception
	 */
	public void register(String event, Receiver<?> receiver) {
		events.register(event, receiver);
	}
	/**
	 * Removes a receiver from a set of registered receivers
	 * @param event event identifier
	 * @param receiver removed receiver
	 * @return {@code true} if {@code receiver} was present and removed
	 */
	public boolean unregister(String event, Receiver<?> receiver) {
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

	/** @return component signature */
	public Signature getSignature() {
		return signature;
	}
	/** @return required entity order */
	public Comparator<Entity> getComparator() {
		return comparator;
	}

	/** @param events event queue and broadcaster accessible to this system */
	void setEvents(EventBroadcaster events) {
		this.events = events;
	}
}
