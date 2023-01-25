package dev.kkorolyov.pancake.platform;

import java.util.Collection;
import java.util.HashSet;

/**
 * A shared bucket of handles/requests for temporarily halting execution of systems.
 * A bit more complicated than a simple boolean to allow for multiple distinct handles to layer on requests to suspend.
 */
public final class Suspend {
	private final Collection<Handle> handles = new HashSet<>();

	/**
	 * Adds {@code handle} to the set of open suspend handles.
	 * Does nothing if {@code handle} is already in the set.
	 */
	public void add(Handle handle) {
		handles.add(handle);
	}
	/**
	 * Removes {@code handle} from the set of open suspend handles.
	 * Does nothing if {@code handle} is not in the set.
	 */
	public void remove(Handle handle) {
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

	/**
	 * A distinct request to suspend.
	 */
	public interface Handle {
		/**
		 * The global shared suspend handle.
		 * Can be used as a top or root level request to suspend when finer detail is unnecessary.
		 */
		Handle GLOBAL = new Handle() {};
	}
}
