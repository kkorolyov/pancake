package dev.kkorolyov.pancake.editor

import dev.kkorolyov.pancake.platform.action.Action
import java.util.ServiceLoader

private val factories by ThreadLocal.withInitial { ServiceLoader.load(ActionWidgetFactory::class.java).toList() }

private val noop by lazy { Widget {} }

/**
 * Returns the most suitable widget for displaying [action] from all [ActionWidgetFactory] providers on the classpath.
 * Falls back to a basic `toString` representation of [action] if no provider found.
 */
fun getActionWidget(action: Action): Widget = factories.firstNotNullOfOrNull { it.get(action) } ?: Widget {
	text(action)
}
/**
 * Returns the most suitable widget for displaying a [c]-type action creator invoking [onNew] from all [ActionWidgetFactory] providers on the classpath.
 * Falls back to a no-op widget if no provider found.
 */
fun getActionWidget(c: Class<Action>, onNew: (Action) -> Unit): Widget = factories.firstNotNullOfOrNull { it.get(c, onNew) } ?: noop

/**
 * Returns widgets drawing given [Action]s.
 */
interface ActionWidgetFactory {
	/**
	 * Returns a widget drawing [action], if this factory handles it.
	 */
	fun get(action: Action): Widget?

	/**
	 * Returns a widget drawing a creator of [c]-type actions and invoking [onNew] with created instances, if this factory handles it.
	 */
	fun get(c: Class<Action>, onNew: (Action) -> Unit): Widget?

	companion object {
		/**
		 * Returns a widget invoking [op] for [action] if it is of type [T].
		 */
		inline fun <reified T : Action> get(action: Action, crossinline op: T.() -> Unit): Widget? = (action as? T)?.let {
			Widget { op(it) }
		}

		/**
		 * Returns a widget invoking [op] with [onNew] if it is for instances of type [T].
		 */
		inline fun <reified T : Action> get(c: Class<out Action>, noinline onNew: (Action) -> Unit, crossinline op: ((T) -> Unit) -> Unit): Widget? = if (c == T::class.java) Widget { op(onNew) } else null
	}
}
