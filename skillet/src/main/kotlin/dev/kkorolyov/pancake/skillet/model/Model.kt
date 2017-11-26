package dev.kkorolyov.pancake.skillet.model

import dev.kkorolyov.pancake.skillet.model.Model.ModelChangeEvent

/**
 * Data model which emits change events.
 */
abstract class Model<T : Model<T>> {
	private val listeners: MutableCollection<ModelListener<T>> = HashSet()

	/**
	 * Distributes a change event to all registered listeners.
	 * @param event specific change event
	 * @return number of listeners which received event
	 */
	protected fun changed(event: ModelChangeEvent): Int =
			this.listeners.onEach { it(this as T, event) }.size

	/**
	 * @param listener new listener
	 * @return `this`
	 */
	fun register(listener: ModelListener<T>): T {
		this.listeners += listener
		return this as T
	}
	/**
	 * @param listener listener to remove
	 * @return `true` if `listener` was registered and is now removed
	 */
	fun unregister(listener: ModelListener<T>): Boolean =
		listeners.removeIf { it == listener }

	/**
	 * Denotes a type of change.
	 */
	interface ModelChangeEvent

}

/**
 * Listens for change events on a `Model`.
 */
typealias ModelListener<T> = (target: T, event: ModelChangeEvent) -> Unit
