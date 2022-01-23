package dev.kkorolyov.pancake.debug.data

/**
 * A set of observable values mirroring some backing state.
 * This model can be periodically refreshed to synchronize mirrored values with backing values.
 */
interface DynamicModel<T : DynamicModel<T>> {
	/**
	 * Synchronizes observable state to backing state.
	 */
	fun refresh()

	/**
	 * Binds this model's observable state to that of [other].
	 */
	fun bind(other: T?)
}
