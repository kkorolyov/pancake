package dev.kkorolyov.pancake.editor

import dev.kkorolyov.pancake.platform.entity.Component
import imgui.flag.ImGuiTableFlags
import java.util.ServiceLoader

private val factories by ThreadLocal.withInitial { ServiceLoader.load(ComponentWidgetFactory::class.java).toList() }

private val noop by lazy { Widget {} }

/**
 * Returns the most suitable widget for displaying [component] from all [ComponentWidgetFactory] providers on the classpath.
 * Falls back to a basic `toString` representation of [component] if no provider found.
 */
fun getComponentWidget(component: Component): Widget = factories.firstNotNullOfOrNull { it.get(component) } ?: Widget {
	text(component)
}
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
 * Returns widgets drawing given [Component]s.
 */
interface ComponentWidgetFactory {
	/**
	 * Returns a widget drawing [component], if this factory handles it.
	 */
	fun get(component: Component): Widget?

	/**
	 * Returns a widget drawing a creator of [c]-type components and invoking [onNew] with created instances, if this factory handles it.
	 */
	fun get(c: Class<Component>, onNew: (Component) -> Unit): Widget?

	companion object {
		/**
		 * Returns a widget invoking [op] for [component] if it is of type [T].
		 */
		inline fun <reified T : Component> get(component: Component, crossinline op: T.() -> Unit): Widget? = (component as? T)?.let {
			Widget { op(it) }
		}

		/**
		 * Returns a widget invoking [op] with [onNew] if it is for instances of type [T].
		 */
		inline fun <reified T : Component> get(c: Class<Component>, noinline onNew: (Component) -> Unit, crossinline op: ((T) -> Unit) -> Unit): Widget? = if (c == T::class.java) Widget { op(onNew) } else null
	}
}
