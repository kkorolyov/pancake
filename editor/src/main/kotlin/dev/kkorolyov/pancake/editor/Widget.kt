package dev.kkorolyov.pancake.editor

/**
 * A drawable element.
 */
interface Widget {
	/**
	 * Renders the current state of this element at the current ImGui stack frame.
	 */
	operator fun invoke()

	companion object {
		/**
		 * Returns a widget running [op] when drawn.
		 */
		inline operator fun invoke(crossinline op: () -> Unit): Widget = object : Widget {
			override fun invoke() = op()
		}
	}
}
