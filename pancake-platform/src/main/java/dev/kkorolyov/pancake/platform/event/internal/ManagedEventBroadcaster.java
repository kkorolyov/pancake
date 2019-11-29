package dev.kkorolyov.pancake.platform.event.internal;

import dev.kkorolyov.pancake.platform.event.Event;
import dev.kkorolyov.pancake.platform.event.EventBroadcaster;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

/**
 * An {@link EventBroadcaster} implementation with exposed management methods.
 */
public class ManagedEventBroadcaster implements EventBroadcaster {
	private final Map<Class<? extends Event>, Set<Consumer<?>>> receivers = new HashMap<>();
	private final Queue<Event> eventQueue = new ArrayDeque<>();

	@Override
	public <E extends Event> EventBroadcaster register(Class<E> type, Consumer<? super E> receiver) {
		receivers.computeIfAbsent(type, k -> new HashSet<>()).add(receiver);
		return this;
	}
	@Override
	public <E extends Event> boolean unregister(Class<E> type, Consumer<? super E> receiver) {
		Set<Consumer<?>> set = receivers.get(type);
		return (set != null) && set.remove(receiver);
	}

	@Override
	public int enqueue(Event event) {
		eventQueue.add(event);

		Set<Consumer<?>> eventReceivers = receivers.get(event.getClass());
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

			Set<Consumer<?>> eventReceivers = receivers.getOrDefault(event.getClass(), Set.of());
			for (Consumer eventReceiver : eventReceivers) {
				eventReceiver.accept(event);
			}
		}
		return size;
	}
}
