package dev.kkorolyov.pancake.platform.event;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Broadcasts events expected by registered systems.
 */
public class EventBroadcaster {
	private final Map<String, Set<Consumer<?>>> receivers = new HashMap<>();
	private final Queue<String> eventQueue = new ArrayDeque<>();
	private final Queue<Object> payloadQueue = new ArrayDeque<>();

	/**
	 * Registers to receive broadcasts of an event.
	 * @param event event identifier
	 * @param receiver action invoked on event reception
	 */
	public void register(String event, Consumer<?> receiver) {
		receivers.computeIfAbsent(event, k -> new HashSet<>()).add(receiver);
	}
	/**
	 * Removes a receiver from a set of registered receivers
	 * @param event event identifier
	 * @param receiver removed receiver
	 * @return {@code true} if {@code receiver} was present and removed
	 */
	public boolean unregister(String event, Consumer<?> receiver) {
		Set<Consumer<?>> set = receivers.get(event);
		return (set != null) && set.remove(receiver);
	}

	/**
	 * Queues an event to broadcast to all registered receivers.
	 * @param event event identifier
	 * @param payload event payload
	 * @return number of receivers registered to event
	 */
	public int enqueue(String event, Object payload) {
		eventQueue.add(event);
		payloadQueue.add(payload);

		Set<Consumer<?>> eventReceivers = receivers.get(event);
		return (eventReceivers == null) ? 0 : eventReceivers.size();
	}

	/**
	 * Dequeues and broadcasts all queued events.
	 * @return number of broadcast events
	 */
	public int broadcast() {
		int size = eventQueue.size();

		while (!eventQueue.isEmpty()) {
			String event = eventQueue.remove();
			Object payload = payloadQueue.remove();

			Set<Consumer<?>> eventReceivers = receivers.get(event);
			if (eventReceivers != null) {
				for (Consumer eventReceiver : eventReceivers) {
					eventReceiver.accept(payload);
				}
			}
		}
		return size;
	}
}
