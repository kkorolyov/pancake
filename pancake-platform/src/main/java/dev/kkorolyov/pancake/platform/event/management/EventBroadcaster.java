package dev.kkorolyov.pancake.platform.event.management;

import dev.kkorolyov.pancake.platform.event.Event;

import java.util.function.Consumer;

/**
 * Broadcasts events expected by registered systems.
 */
public interface EventBroadcaster {
	/**
	 * Registers to receive broadcasts of an event.
	 * @param type event type
	 * @param receiver action invoked on event reception
	 */
	<E extends Event> void register(Class<E> type, Consumer<? super E> receiver);
	/**
	 * Removes a receiver from a set of registered receivers
	 * @param type event type
	 * @param receiver removed receiver
	 * @return {@code true} if {@code receiver} was present and removed
	 */
	<E extends Event> boolean unregister(Class<E> type, Consumer<? super E> receiver);

	/**
	 * Queues an event to broadcast to all registered receivers.
	 * @param event event to queue
	 * @return number of receivers registered to event
	 */
	int enqueue(Event event);
}
