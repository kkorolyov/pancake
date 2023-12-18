package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Widget
import org.slf4j.LoggerFactory

private val log = LoggerFactory.getLogger(WindowManifest::class.java)

/**
 * Renders a persistent collection of windows bound to unique keys of type [T].
 */
class WindowManifest<T> : Widget {
	private val windows = mutableMapOf<T, Window>()
	private var recent: Window? = null

	/**
	 * The number of windows in this manifest.
	 */
	val size: Int
		get() = windows.size

	/**
	 * Returns the window bound to [key], if any.
	 */
	operator fun get(key: T): Window? = windows[key]
	/**
	 * Focuses the current window bound to [key] if it exists, else binds the window returned from [initializer].
	 */
	operator fun set(key: T, initializer: () -> Window) {
		recent = windows.getOrPut(key) {
			val window = initializer()
			log.debug("add window {{}}", window.label)
			window
		}

		recent?.visible = true
	}

	override fun invoke() {
		// cleanup closed windows
		windows.values.removeAll {
			val result = !it.visible
			if (result) log.debug("drop window {{}}", it.label)
			result
		}

		// render current windows
		windows.values.forEach {
			if (it === recent) {
				log.debug("focus window {{}}", it.label)
				it.focus()
			} else it()
		}

		recent = null
	}
}
