package dev.kkorolyov.pancake.platform;

import java.util.Collection;
import java.util.HashSet;

/**
 * A shared bucket of handles/requests for temporarily halting execution of systems.
 * A bit more complicated than a simple boolean to allow for multiple distinct handles to layer on requests to suspend.
 */
public final class Suspend {
	private final Collection<Object> handles = new HashSet<>();

	/**
	 * Adds {@code handle} to the set of open suspend handles.
	 * Does nothing if {@code handle} is already in the set.
	 */
	public void add(Object handle) {
		handles.add(handle);
	}
	/**
	 * Removes {@code handle} from the set of open suspend handles.
	 * Does nothing if {@code handle} is not in the set.
	 */
	public void remove(Object handle) {
		handles.remove(handle);
	}

	/**
	 * Returns {@code true} if this has at least one open handle.
	 */
	public boolean isActive() {
		return !handles.isEmpty();
	}

	@Override
	public String toString() {
		return "Suspend{" +
				"handles=" + handles +
				'}';
	}
}
