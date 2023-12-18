package dev.kkorolyov.pancake.editor

/**
 * A drawable element.
 */
interface Widget {
	/**
	 * Draws the current state of this widget at the current ImGui stack frame.
	 */
	operator fun invoke()

	companion object {
		/**
		 * An empty widget.
		 * Can be used as a placeholder.
		 */
		val NOOP = Widget {}

		/**
		 * Returns a widget running [op] when drawn.
		 */
		inline operator fun invoke(crossinline op: () -> Unit): Widget = object : Widget {
			override fun invoke() = op()
		}
	}
}
