package dev.kkorolyov.pancake.graphics.gl.internal

/**
 * Memoizes the return of [initializer] and can also be manually invalidated.
 */
class Cache<T>(private val initializer: () -> T) {
	private var value: T? = null

	/**
	 * Whether this cache currently has a value.
	 */
	val initialized: Boolean
		get() = value != null

	/**
	 * Returns the value after initializing it if not yet done so.
	 */
	operator fun invoke(): T {
		if (value == null) {
			synchronized(this) {
				if (value == null) {
					value = initializer()
					return value!!
				}
			}
		}
		return value!!
	}

	/**
	 * Invalidates the current value.
	 * The subsequent [invoke] call will reinitialize it.
	 */
	fun invalidate() {
		synchronized(this) {
			value = null
		}
	}
}
