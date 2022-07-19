package dev.kkorolyov.pancake.editor

import dev.kkorolyov.pancake.editor.data.BasicComponentData
import dev.kkorolyov.pancake.editor.view.BasicComponentDetails
import dev.kkorolyov.pancake.platform.entity.Component
import java.util.*

private val factories: ThreadLocal<Collection<ComponentDataFactory>> =
	ThreadLocal.withInitial { ServiceLoader.load(ComponentDataFactory::class.java).toList() }

/**
 * Returns the most suitable dynamic model wrapping [component].
 * Looks for any handling [ComponentDataFactory] providers on the classpath and defaults to a [BasicComponentData] if no provider handles.
 */
fun getComponentData(component: Component): ComponentData<*, *> =
	factories.get().firstNotNullOfOrNull { it.getData(component) } ?: BasicComponentData(component)

/**
 * Returns the most suitable details fragment for [data].
 * Looks for any handling [ComponentDataFactory] providers on the classpath and defaults to a [BasicComponentDetails] if no provider handles.
 */
fun getComponentDetails(data: ComponentData<*, *>): ComponentDetails =
	factories.get().firstNotNullOfOrNull { it.getDetails(data) } ?: BasicComponentDetails(data)

/**
 * Returns dynamic models and details fragments for a given type of component.
 */
interface ComponentDataFactory {
	/**
	 * Returns a dynamic model wrapping [component], if its runtime type is handled by this factory.
	 */
	fun getData(component: Component): ComponentData<*, *>?

	/**
	 * Returns a details fragment for [data], if its runtime type is handled by this factory.
	 */
	fun getDetails(data: ComponentData<*, *>): ComponentDetails?
}
