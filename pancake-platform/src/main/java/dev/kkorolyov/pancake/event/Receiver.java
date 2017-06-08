package dev.kkorolyov.pancake.event;

import dev.kkorolyov.pancake.Component;
import dev.kkorolyov.pancake.Entity;

/**
 * Acts on received events.
 */
@FunctionalInterface
public interface Receiver {
	/**
	 * Receives and acts on a single event.
	 * @param target entity affected by event, or {@code null} if not applicable
	 * @param changed component affected by event, or {@code null} if not applicable
	 */
	void receive(Entity target, Component changed);
}
