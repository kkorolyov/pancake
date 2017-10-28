package dev.kkorolyov.pancake.skillet.model;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * An easily-packaged, portable object.
 */
public abstract class Storable<T extends Storable<T>> {
	private final Collection<StorableListener<T>> listeners = new CopyOnWriteArrayList<>();

	/**
	 * Distributes a change event to all registered listeners.
	 * @param event specific change event
	 * @return number of listeners which received event
	 */
	protected int changed(StorableChangeEvent event) {
		for (StorableListener<T> listener : listeners) listener.changed((T) this, event);

		return listeners.size();
	}

	/**
	 * @param listener new listener
	 * @return {@code this}
	 */
	public T register(StorableListener<T> listener) {
		listeners.add(listener);
		return (T) this;
	}
	/**
	 * @param listener listener to remove
	 * @return {@code true} if {@code listener} was registered and is now removed
	 */
	public boolean unregister(StorableListener<T> listener) {
		if (!listeners.contains(listener)) return false;

		listeners.remove(listener);
		return true;
	}

	/**
	 * Denotes a type of change.
	 */
	public interface StorableChangeEvent {}
}
