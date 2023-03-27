package dev.kkorolyov.pancake.editor.factory

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.getValue
import dev.kkorolyov.pancake.editor.text
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
interface ActionWidgetFactory : WidgetFactory<Action> {
	companion object {
		/**
		 * Returns a widget invoking [op] for [action] if it is of type [T].
		 */
		inline fun <reified T : Action> get(action: Action, crossinline op: T.() -> Widget): Widget? = (action as? T)?.let(op)

		/**
		 * Returns a widget invoking [op] with [onNew] if it is for instances of type [T].
		 */
		inline fun <reified T : Action> get(c: Class<out Action>, noinline onNew: (Action) -> Unit, crossinline op: ((T) -> Unit) -> Widget): Widget? = if (c == T::class.java) op(onNew) else null
	}
}
