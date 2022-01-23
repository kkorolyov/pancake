package dev.kkorolyov.pancake.debug

import dev.kkorolyov.pancake.debug.data.BasicComponentData
import dev.kkorolyov.pancake.debug.view.BasicComponentDetails
import dev.kkorolyov.pancake.platform.entity.Component
import java.util.*

private val factories: ThreadLocal<Collection<ComponentDataFactory>> =
	ThreadLocal.withInitial { ServiceLoader.load(ComponentDataFactory::class.java).toList() }

fun getComponentData(component: Component): ComponentData<*, *> =
	factories.get().firstNotNullOfOrNull { it.getData(component) } ?: BasicComponentData(component)

fun getComponentDetails(data: ComponentData<*, *>): ComponentDetails =
	factories.get().firstNotNullOfOrNull { it.getDetails(data) } ?: BasicComponentDetails(data)

interface ComponentDataFactory {
	fun getData(component: Component): ComponentData<*, *>?
	fun getDetails(data: ComponentData<*, *>): ComponentDetails?
}
