package dev.kkorolyov.pancake.platform.event;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Supports registration of event handlers and enqueuing of event instances.
 */
public interface EventLoop {
	/**
	 * Registers to receive broadcasts of an event.
	 * @param type event type
	 * @param receiver action invoked on event reception
	 */
	<E extends Event> void register(Class<E> type, Consumer<? super E> receiver);

	/**
	 * Queues an event to broadcast to all registered receivers.
	 * @param event event to queue
	 */
	void enqueue(Event event);

	/**
	 * An {@link EventLoop} implementation supporting broadcasting events.
	 */
	final class Broadcasting implements EventLoop, Iterable<Event> {
		private final Map<Class<? extends Event>, Set<Consumer<?>>> receivers = new HashMap<>();
		private final Queue<Event> eventQueue = new ArrayDeque<>();

		@Override
		public <E extends Event> void register(Class<E> type, Consumer<? super E> receiver) {
			receivers.computeIfAbsent(type, k -> new HashSet<>()).add(receiver);
		}

		@Override
		public void enqueue(Event event) {
			eventQueue.add(event);
		}

		/**
		 * Dequeues and broadcasts all queued events.
		 */
		public void broadcast() {
			for (Event event = eventQueue.poll(); event != null; event = eventQueue.poll()) {
				for (Consumer eventReceiver : receivers.getOrDefault(event.getClass(), Set.of())) {
					eventReceiver.accept(event);
				}
			}
		}

		@Override
		public Iterator<Event> iterator() {
			return eventQueue.iterator();
		}

		@Override
		public String toString() {
			return "Managed{" +
					"receivers=" + receivers +
					", eventQueue=" + eventQueue +
					'}';
		}
	}
}
