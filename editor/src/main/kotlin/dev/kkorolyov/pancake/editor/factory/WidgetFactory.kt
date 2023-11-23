package dev.kkorolyov.pancake.editor.factory

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.column
import dev.kkorolyov.pancake.editor.getValue
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.table
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.platform.GameSystem
import imgui.flag.ImGuiTableFlags
import java.util.ServiceLoader
import kotlin.math.roundToInt

private val factories by ThreadLocal.withInitial { ServiceLoader.load(WidgetFactory::class.java).groupBy(WidgetFactory<*>::type) }
private val noop by lazy { Widget {} }

/**
 * Returns a widget displaying basic data (data common to all systems) of [system].
 */
fun basicGameSystemWidget(system: GameSystem): Widget = Widget {
	table("gameSystem", 2, flags = ImGuiTableFlags.SizingStretchProp) {
		column { text("Tick time (ns)") }
		column { text(system.sampler.value) }

		column { text("TPS") }
		column { text((1e9 / system.sampler.value).roundToInt()) }

		column { text("Signature") }
		column {
			list("##signature") {
				system.forEach {
					text(it.simpleName)
				}
			}
		}
	}
}

/**
 * Returns the most suitable widget for displaying [t] from all [c]-type [WidgetFactory] providers on the classpath.
 * Falls back to the default widget for [t]'s base type if no suitable provider found.
 */
fun <T> getWidget(c: Class<T>, t: T): Widget = factories[c]?.firstNotNullOfOrNull { (it as WidgetFactory<T>).get(t) } ?: when (t) {
	is GameSystem -> basicGameSystemWidget(t)
	else -> Widget { text(t ?: null.toString()) }
}
/**
 * Returns the most suitable widget for displaying an [sc]-type component creator invoking [onNew] from all [c]-type [WidgetFactory] providers on the classpath.
 * Falls back to a no-op widget if no suitable provider found.
 */
fun <T, ST : Class<out T>> getWidget(c: Class<T>, sc: ST, onNew: (T) -> Unit): Widget = factories[c]?.firstNotNullOfOrNull { (it as WidgetFactory<T>).get(sc, onNew) } ?: noop

/**
 * Returns widgets responsible for displaying, editing, or creating [T] instances.
 * Designed for use as a service provider.
 */
interface WidgetFactory<T> {
	/**
	 * The base class of thing this factory provides widgets for.
	 */
	val type: Class<T>

	/**
	 * Returns a widget drawing [t], if this factory handles doing so.
	 */
	fun get(t: T): Widget?

	/**
	 * Returns a widget drawing a creator of [c]-type subclass of [T] and invoking [onNew] with created instances, if this factory handles doing so.
	 */
	fun get(c: Class<out T>, onNew: (T) -> Unit): Widget?

	companion object {
		/**
		 * Returns the result of invoking [op] for [t] if it is of type [T].
		 */
		inline fun <reified T> get(t: Any, crossinline op: T.() -> Widget): Widget? = (t as? T)?.let(op)

		/**
		 * Returns the result of invoking [op] with [onNew] if [c] type matches [T].
		 */
		inline fun <reified T> get(c: Class<out Any>, noinline onNew: (T) -> Unit, crossinline op: ((T) -> Unit) -> Widget): Widget? = if (c == T::class.java) op(onNew) else null
	}
}
