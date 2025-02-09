package dev.kkorolyov.pancake.editor

import kotlin.reflect.KProperty

/**
 * Adapts [ThreadLocal] to use as a get delegate.
 */
operator fun <T> ThreadLocal<T>.getValue(thisRef: Any?, property: KProperty<*>): T = get()
/**
 * Adapts [ThreadLocal] to use as a set delegate.
 */
operator fun <T> ThreadLocal<T>.setValue(thisRef: Any?, property: KProperty<*>, value: T): Unit = set(value)

