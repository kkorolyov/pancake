package dev.kkorolyov.pancake.editor.factory

import dev.kkorolyov.pancake.editor.Widget

/**
 * Returns widgets responsible for displaying, editing, or creating [T] instances.
 * Designed for use as a service provider.
 */
interface WidgetFactory<T> {
	/**
	 * Returns a widget drawing [t], if this factory handles doing so.
	 */
	fun get(t: T): Widget?

	/**
	 * Returns a widget drawing a creator of [c]-type subclass of [T] and invoking [onNew] with created instances, if this factory handles doing so.
	 */
	fun get(c: Class<T>, onNew: (T) -> Unit): Widget?
}
