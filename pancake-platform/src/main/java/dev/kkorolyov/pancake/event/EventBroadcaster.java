package dev.kkorolyov.pancake.event;

import dev.kkorolyov.pancake.Config;
import dev.kkorolyov.simplelogs.Logger;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Broadcasts events expected by registered systems.
 */
public class EventBroadcaster {
	private static final Logger log = Config.getLogger(EventBroadcaster.class);

	private final Map<String, Set<Receiver>> receivers = new HashMap<>();
	private final Queue<String> eventQueue = new ArrayDeque<>();
	private final Queue<Object> payloadQueue = new ArrayDeque<>();

	/**
	 * Registers to receive broadcasts of an event.
	 * @param event event identifier
	 * @param receiver action invoked on event reception
	 */
	public void register(String event, Receiver<?> receiver) {
		receivers.computeIfAbsent(event, k -> new HashSet<>()).add(receiver);
		log.info("Registered new event receiver: ({}, {})", event, receiver);
	}
	/**
	 * Removes a receiver from a set of registered receivers
	 * @param event event identifier
	 * @param receiver removed receiver
	 * @return {@code true} if {@code receiver} was present and removed
	 */
	public boolean unregister(String event, Receiver<?> receiver) {
		Set<Receiver> set = receivers.get(event);
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
		log.debug("Enqueued new event: ({}, {})", event, payload);

		Set<Receiver> eventReceivers = receivers.get(event);
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

			Set<Receiver> eventReceivers = receivers.get(event);
			if (eventReceivers != null) {
				for (Receiver eventReceiver : eventReceivers) {
					eventReceiver.receive(payload);
				}
			}
		}
		if (size > 0) log.info("Broadcast {} queued events", size);
		return size;
	}
}
