package dev.kkorolyov.pancake.skillet.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * A data model which emits change events.
 */
public abstract class Model<T extends Model<T>> {
	private final Collection<ModelListener<T>> listeners = new HashSet<>();

	/**
	 * Distributes a change event to all registered listeners.
	 * @param event specific change event
	 * @return number of listeners which received event
	 */
	protected int changed(ModelChangeEvent event) {
		for (ModelListener<T> listener : listeners) listener.changed((T) this, event);

		return listeners.size();
	}

	/**
	 * @param listener new listener
	 * @return {@code this}
	 */
	public T register(ModelListener<T> listener) {
		listeners.add(listener);
		return (T) this;
	}
	/**
	 * @param listener listener to remove
	 * @return {@code true} if {@code listener} was registered and is now removed
	 */
	public boolean unregister(ModelListener<T> listener) {
		Iterator<ModelListener<T>> it = listeners.iterator();
		while (it.hasNext()) {
			if (it.next().equals(listener)) {
				it.remove();
				return true;
			}
		}
		return false;
	}

	/**
	 * Denotes a type of change.
	 */
	public interface ModelChangeEvent {}
}
