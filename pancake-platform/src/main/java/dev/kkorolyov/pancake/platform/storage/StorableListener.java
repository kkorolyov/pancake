package dev.kkorolyov.pancake.platform.storage;

import dev.kkorolyov.pancake.platform.storage.Storable.StorableChangeEvent;

/**
 * Listens for changes on a {@link Storable}.
 */
@FunctionalInterface
public interface StorableListener<T extends Storable<T>> {
	/**
	 * Invoked when the observable this is attached to changes.
	 * @param target changed observable
	 */
	void changed(T target, StorableChangeEvent event);
}
