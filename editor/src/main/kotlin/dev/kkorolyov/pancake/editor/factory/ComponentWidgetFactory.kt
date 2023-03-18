package dev.kkorolyov.pancake.editor.factory

import dev.kkorolyov.pancake.editor.Op
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.column
import dev.kkorolyov.pancake.editor.getValue
import dev.kkorolyov.pancake.editor.table
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.platform.entity.Component
import imgui.flag.ImGuiTableFlags
import java.util.ServiceLoader

private val factories by ThreadLocal.withInitial { ServiceLoader.load(ComponentWidgetFactory::class.java).toList() }

private val noop by lazy { Widget {} }

/**
 * Returns a widget displaying the `toString` representation of [component].
 */
fun basicComponentWidget(component: Component): Widget = Widget { text(component) }

/**
 * Returns the most suitable widget for displaying [component] from all [ComponentWidgetFactory] providers on the classpath.
 * Falls back to [basicComponentWidget] if no provider found.
 */
fun getComponentWidget(component: Component): Widget = factories.firstNotNullOfOrNull { it.get(component) } ?: basicComponentWidget(component)
/**
 * Returns the most suitable widget for displaying a [c]-type component creator invoking [onNew] from all [ComponentWidgetFactory] providers on the classpath.
 * Falls back to a no-op widget if no provider found.
 */
fun getComponentWidget(c: Class<Component>, onNew: (Component) -> Unit): Widget = factories.firstNotNullOfOrNull { it.get(c, onNew) } ?: noop

/**
 * Helper for drawing a 2-column table suited for displaying property `(name, value)` pairs.
 * Labels the table [id] and invokes [op] within the table.
 */
inline fun propertiesTable(id: String, op: Op) {
	table(id, 2, ImGuiTableFlags.SizingStretchProp, op)
}
/**
 * Helper for drawing a 2-column row suited for displaying property [label] with [op].
 */
inline fun propertyRow(label: String, op: Op) {
	column { text(label) }
	column(op)
}

/**
 * Returns widgets drawing [Component]s.
 */
interface ComponentWidgetFactory : WidgetFactory<Component> {
	companion object {
		/**
		 * Returns the result of invoking [op] for [component] if it is of type [T].
		 */
		inline fun <reified T : Component> get(component: Component, crossinline op: T.() -> Widget): Widget? = (component as? T)?.let(op)

		/**
		 * Returns the result of invoking [op] with [onNew] if it is for instances of type [T].
		 */
		inline fun <reified T : Component> get(c: Class<Component>, noinline onNew: (Component) -> Unit, crossinline op: ((T) -> Unit) -> Widget): Widget? = if (c == T::class.java) op(onNew) else null
	}
}
