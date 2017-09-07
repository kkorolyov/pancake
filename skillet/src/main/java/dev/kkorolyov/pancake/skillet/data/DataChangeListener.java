package dev.kkorolyov.pancake.skillet.data;

import dev.kkorolyov.pancake.skillet.data.DataObservable.DataChangeEvent;

/**
 * Listens for changes on a {@link DataObservable}.
 */
@FunctionalInterface
public interface DataChangeListener<T extends DataObservable<T>> {
	/**
	 * Invoked when the observable this is attached to changes.
	 * @param target changed observable
	 */
	void changed(T target, DataChangeEvent event);
}
