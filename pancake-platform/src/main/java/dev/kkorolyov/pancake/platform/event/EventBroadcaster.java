package dev.kkorolyov.pancake.platform.event;

import java.util.function.Consumer;

/**
 * Broadcasts events expected by registered systems.
 */
public interface EventBroadcaster {
	/**
	 * Registers to receive broadcasts of an event.
	 * @param event event identifier
	 * @param receiver action invoked on event reception
	 */
	void register(String event, Consumer<?> receiver);
	/**
	 * Removes a receiver from a set of registered receivers
	 * @param event event identifier
	 * @param receiver removed receiver
	 * @return {@code true} if {@code receiver} was present and removed
	 */
	boolean unregister(String event, Consumer<?> receiver);

	/**
	 * Queues an event to broadcast to all registered receivers.
	 * @param event event identifier
	 * @param payload event payload
	 * @return number of receivers registered to event
	 */
	int enqueue(String event, Object payload);
}
