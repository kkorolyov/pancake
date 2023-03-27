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

private val factories by ThreadLocal.withInitial { ServiceLoader.load(GameSystemWidgetFactory::class.java).toList() }

/**
 * Returns a widget displaying basic data (data common to all systems) of [system].
 */
fun basicGameSystemWidget(system: GameSystem): Widget = Widget {
	table("gameSystem", 2, ImGuiTableFlags.SizingStretchProp) {
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
 * Returns the most suitable widget for displaying [system] from all [GameSystemWidgetFactory] providers on the classpath.
 * Falls back to [basicGameSystemWidget] if no provider found.
 */
fun getGameSystemWidget(system: GameSystem): Widget = factories.firstNotNullOfOrNull { it.get(system) } ?: basicGameSystemWidget(system)

/**
 * Returns widgets drawing [GameSystem]s.
 */
interface GameSystemWidgetFactory : WidgetFactory<GameSystem> {
	companion object {
		/**
		 * Returns the result of invoking [op] for [system] if it is of type [T].
		 */
		inline fun <reified T : GameSystem> get(system: GameSystem, crossinline op: T.() -> Widget): Widget? = (system as? T)?.let(op)

		/**
		 * Returns the result of invoking [op] with [onNew] if it is for instances of type [T].
		 */
		inline fun <reified T : GameSystem> get(c: Class<GameSystem>, noinline onNew: (GameSystem) -> Unit, crossinline op: ((T) -> Unit) -> Widget): Widget? = if (c == T::class.java) op(onNew) else null
	}
}
