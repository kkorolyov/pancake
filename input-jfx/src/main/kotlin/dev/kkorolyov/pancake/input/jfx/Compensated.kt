package dev.kkorolyov.pancake.input.jfx

/**
 * Stateful supplier of compensatable [T] instances.
 */
class Compensated<T>(private val value: T, private val compensating: T) {
	private var isActive: Boolean = false

	/**
	 * Activates and returns [value] if not active, else `null`.
	 */
	fun get(): T? {
		return if (!isActive) {
			isActive = true
			value
		} else null
	}

	/**
	 * Deactivates and returns [compensating] if active, else `null`.
	 */
	fun compensate(): T? {
		return if (isActive) {
			isActive = false
			compensating
		} else null
	}
}
