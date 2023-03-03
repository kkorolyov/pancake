package dev.kkorolyov.pancake.graphics.util

import kotlin.reflect.KProperty

/**
 * Memoizes the return of [initializer] and can also be manually invalidated.
 */
class Cache<T>(private val initializer: () -> T) {
	@Volatile
	private var value: T? = null

	/**
	 * Whether this cache currently has a value.
	 */
	val initialized: Boolean
		get() = value != null

	/**
	 * For using this cache as a delegate property.
	 * Returns the value stored in this cache - initializing it if not yet done so.
	 */
	operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
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
	 * Invalidates the current value, if it is initialized.
	 * If specified, invokes [block] with the current value before invalidating it.
	 */
	fun invalidate(block: ((T) -> Unit)? = null) {
		if (initialized) {
			synchronized(this) {
				if (initialized) {
					block?.invoke(value!!)
					value = null
				}
			}
		}
	}
}
