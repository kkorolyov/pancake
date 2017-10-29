package dev.kkorolyov.pancake.skillet.model;

import dev.kkorolyov.pancake.skillet.model.Model.ModelChangeEvent;

/**
 * Listens for changes on a {@link Model}.
 */
@FunctionalInterface
public interface ModelListener<T extends Model<T>> {
	/**
	 * Invoked when the model this is attached to changes.
	 * @param target changed model
	 */
	void changed(T target, ModelChangeEvent event);
}
