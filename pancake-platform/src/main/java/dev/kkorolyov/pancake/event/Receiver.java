package dev.kkorolyov.pancake.event;

/**
 * Acts on received events.
 */
@FunctionalInterface
public interface Receiver<T> {
	/**
	 * Receives and acts on a single event.
	 * @param payload event payload
	 */
	void receive(T payload);
}
