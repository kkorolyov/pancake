package dev.kkorolyov.pancake.platform.event;

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
	private final Map<String, Set<Consumer<?>>> receivers = new HashMap<>();
	private final Queue<String> eventQueue = new ArrayDeque<>();
	private final Queue<Object> payloadQueue = new ArrayDeque<>();

	@Override
	public void register(String event, Consumer<?> receiver) {
		receivers.computeIfAbsent(event, k -> new HashSet<>()).add(receiver);
	}
	@Override
	public boolean unregister(String event, Consumer<?> receiver) {
		Set<Consumer<?>> set = receivers.get(event);
		return (set != null) && set.remove(receiver);
	}

	@Override
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
