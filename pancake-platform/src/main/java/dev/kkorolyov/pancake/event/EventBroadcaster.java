package dev.kkorolyov.pancake.event;

import java.util.*;

import dev.kkorolyov.pancake.Component;
import dev.kkorolyov.pancake.Config;
import dev.kkorolyov.pancake.Entity;
import dev.kkorolyov.simplelogs.Logger;

/**
 * Broadcasts events expected by registered systems.
 */
public class EventBroadcaster {
	private static final Logger log = Config.getLogger(EventBroadcaster.class);

	private final Map<String, Set<Receiver>> receivers = new HashMap<>();
	private final Queue<Event> eventQueue = new ArrayDeque<>();

	/**
	 * Registers to receive broadcasts of an event.
	 * @param event event identifier
	 * @param receiver action invoked on event reception
	 */
	public void register(String event, Receiver receiver) {
		receivers.computeIfAbsent(event, k -> new HashSet<>()).add(receiver);
		log.info("Registered new event-{} receiver: {}", event, receiver);
	}
	/**
	 * Removes a receiver from a set of registered receivers
	 * @param event event identifier
	 * @param receiver removed receiver
	 * @return {@code true} if {@code receiver} was present and removed
	 */
	public boolean unregister(String event, Receiver receiver) {
		Set<Receiver> set = receivers.get(event);
		return (set != null) && set.remove(receiver);
	}

	/**
	 * Queues an event to broadcast to all registered receivers.
	 * @param event event identifier
	 * @param target entity affected by event, or {@code null} if not applicable
	 * @param changed component affected by event, or {@code null} if not applicable
	 * @return number of receivers registered to event
	 */
	public int enqueue(String event, Entity target, Component changed) {
		eventQueue.add(new Event(event, target, changed));
		log.info("Enqueued new event: ({}, {}, {})", event, target, changed);

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
			Event event = eventQueue.remove();

			Set<Receiver> eventReceivers = receivers.get(event.name);
			if (eventReceivers != null) {
				for (Receiver eventReceiver : eventReceivers) {
					eventReceiver.receive(event.target, event.changed);
				}
			}
		}
		if (size > 0) log.debug("Broadcast {} queued events", size);
		return size;
	}

	private class Event {
		final String name;
		final Entity target;
		final Component changed;

		Event(String name, Entity target, Component changed) {
			this.name = name;
			this.target = target;
			this.changed = changed;
		}
	}
}
