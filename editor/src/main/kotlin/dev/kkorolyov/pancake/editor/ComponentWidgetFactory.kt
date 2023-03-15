package dev.kkorolyov.pancake.editor

import dev.kkorolyov.pancake.platform.entity.Component
import imgui.flag.ImGuiTableFlags
import java.util.ServiceLoader

private val factories = ThreadLocal.withInitial { ServiceLoader.load(ComponentWidgetFactory::class.java).toList() }

/**
 * Returns the most suitable widget for displaying [component] by traversing [ComponentWidgetFactory] providers on the classpath.
 * Falls back to a basic `toString` representation of [component].
 */
fun getComponentWidget(component: Component): Widget = factories.get().firstNotNullOfOrNull { it.get(component) } ?: Widget {
	text(component)
}

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
 * Returns widgets rendering given [Component]s.
 */
interface ComponentWidgetFactory {
	/**
	 * Returns a widget rendering [component], if this factory handles it.
	 */
	fun get(component: Component): Widget?

	companion object {
		/**
		 * Returns a widget invoking [op] for [component] if it is of type [T].
		 */
		inline fun <reified T : Component> get(component: Component, crossinline op: T.() -> Unit): Widget? = (component as? T)?.let {
			Widget { op(it) }
		}
	}
}
