package dev.kkorolyov.pancake;

import dev.kkorolyov.pancake.event.EventBroadcaster;
import dev.kkorolyov.pancake.event.Receiver;

/**
 * Performs work on entities matching a certain component signature.
 */
public abstract class GameSystem {
	private final Signature signature;
	private EventBroadcaster events;

	/**
	 * Constructs a new system.
	 * @param signature signature defining all components an entity must have to be affected by this system
	 */
	public GameSystem(Signature signature) {
		this.signature = signature;
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
	public void register(String event, Receiver receiver) {
		events.register(event, receiver);
	}
	/**
	 * Removes a receiver from a set of registered receivers
	 * @param event event identifier
	 * @param receiver removed receiver
	 * @return {@code true} if {@code receiver} was present and removed
	 */
	public boolean unregister(String event, Receiver receiver) {
		return events.unregister(event, receiver);
	}

	/**
	 * Queues an event.
	 * @param event event identifier
	 * @param target entity affected by event, or {@code null} if not applicable
	 * @param changed component affected by event, or {@code null} if not applicable
	 */
	public void enqueue(String event, Entity target, Component changed) {
		events.enqueue(event, target, changed);
	}

	/** @return component signature */
	public Signature getSignature() {
		return signature;
	}

	/** @param events event queue and broadcaster used by this system */
	void setEvents(EventBroadcaster events) {
		this.events = events;
	}
}
