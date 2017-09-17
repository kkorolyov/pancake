package dev.kkorolyov.pancake.muffin.data;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A piece of data which fires change events.
 */
public abstract class DataObservable<T extends DataObservable<T>> {
	private final Collection<DataChangeListener<T>> listeners = new CopyOnWriteArrayList<>();

	/**
	 * Distributes a change event to all registered listeners.
	 * @return number of listeners which received event
	 */
	protected int changed(DataChangeEvent event) {
		for (DataChangeListener<T> listener : listeners) listener.changed((T) this, event);

		return listeners.size();
	}

	/**
	 * @param listener new listener
	 * @return {@code this}
	 */
	public T register(DataChangeListener<T> listener) {
		listeners.add(listener);
		return (T) this;
	}
	/**
	 * @param listener listener to remove
	 * @return {@code true} if {@code listener} was registered and is now removed
	 */
	public boolean unregister(DataChangeListener<T> listener) {
		if (!listeners.contains(listener)) return false;

		listeners.remove(listener);
		return true;
	}

	public interface DataChangeEvent {

	}
}
