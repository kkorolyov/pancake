package dev.kkorolyov.pancake.editor

import dev.kkorolyov.pancake.platform.entity.Component
import imgui.ImGui
import java.util.ServiceLoader

private val factories = ThreadLocal.withInitial { ServiceLoader.load(ComponentWidgetFactory::class.java).toList() }

/**
 * Returns the most suitable widget for displaying [component] by traversing [ComponentWidgetFactory] providers on the classpath.
 * Falls back to a basic `toString` representation of [component].
 */
fun getComponentWidget(component: Component): Widget = factories.get().firstNotNullOfOrNull { it.get(component) } ?: Widget {
	ImGui.text(component.toString())
}

/**
 * Returns widgets rendering given [Component]s.
 */
interface ComponentWidgetFactory {
	/**
	 * Returns a widget rendering [component], if this factory handles it.
	 */
	fun get(component: Component): Widget?
}
