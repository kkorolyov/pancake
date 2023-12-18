package dev.kkorolyov.pancake.editor

import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.io.Structizers
import kotlin.reflect.KProperty

/**
 * Adapts [ThreadLocal] to use as a get delegate.
 */
operator fun <T> ThreadLocal<T>.getValue(thisRef: Any?, property: KProperty<*>): T = get()
/**
 * Adapts [ThreadLocal] to use as a set delegate.
 */
operator fun <T> ThreadLocal<T>.setValue(thisRef: Any?, property: KProperty<*>, value: T): Unit = set(value)

/**
 * Returns [entity] as a simple serializable structure.
 * Any components without a registered [dev.kkorolyov.pancake.platform.io.Structizer] are written as the string `NOT SUPPORTED`.
 */
fun toStructEntity(entity: Entity): Map<String, Any> = entity.associate { it::class.java.name to structizeComponent(it) }

private fun structizeComponent(component: Component) =
	try {
		Structizers.toStruct(component)
	} catch (e: IllegalArgumentException) {
		"NOT SUPPORTED"
	}
