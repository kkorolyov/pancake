package dev.kkorolyov.pancake.editor.ext

import imgui.type.ImBoolean
import imgui.type.ImDouble

/**
 * Returns a pointer to [this] value.
 */
fun Boolean.ptr(): ImBoolean = ImBoolean(this)
/**
 * Returns a pointer to [this] value.
 */
fun Double.ptr(): ImDouble = ImDouble(this)
