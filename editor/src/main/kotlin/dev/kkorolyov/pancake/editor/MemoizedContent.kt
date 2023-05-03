package dev.kkorolyov.pancake.editor

private val noop by lazy { Widget {} }

/**
 * Contains a memoized widget derived from keys using [generator].
 */
class MemoizedContent<K>(private val generator: (K) -> Widget, private val defaultValue: Widget = noop) {
	/**
	 * The current contained widget.
	 */
	val value: Widget by ::_value

	private var current: K? = null
	private var _value: Widget = defaultValue

	/**
	 * Sets [key] on this content.
	 * If the new key differs from the current, regenerates [value] using [key].
	 */
	operator fun invoke(key: K) {
		if (key != current) {
			current = key
			_value = generator(key)
		}
	}

	/**
	 * Clears the current key and sets [value] to [defaultValue].
	 */
	fun reset() {
		current = null
		_value = defaultValue
	}
}
