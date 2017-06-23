package dev.kkorolyov.pancake.event;

import dev.kkorolyov.pancake.entity.Component;
import dev.kkorolyov.pancake.entity.Entity;

/**
 * Acts on received events.
 */
@FunctionalInterface
public interface Receiver {
	/**
	 * Receives and acts on a single event.
	 * @param target entity affected by event, or {@code null} if not applicable
	 * @param rawTarget components affected by event, or {@code null} if not applicable
	 */
	void receive(Entity target, Iterable<Component> rawTarget);
}
