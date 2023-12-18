package dev.kkorolyov.pancake.editor

/**
 * Stores and returns the last [V] value generated from a new [K] key using [mapper].
 * For reducing redundant identical-input calls to some function (in this case, [mapper]).
 */
class DebouncedValue<K, V>(private val mapper: (K) -> V) {
	private var currentKey: K? = null
	private var currentValue: V? = null

	/**
	 * Returns the current value, if any.
	 */
	fun get(): V? = currentValue
	/**
	 * Sets the new [key].
	 * If [key] is different from the current key, regenerates the stored value using [key].
	 * Returns the current value - or the new value, if regenerated.
	 */
	fun set(key: K): V {
		if (key != currentKey) {
			currentKey = key
			currentValue = mapper(key)
		}
		return currentValue!!
	}
}
