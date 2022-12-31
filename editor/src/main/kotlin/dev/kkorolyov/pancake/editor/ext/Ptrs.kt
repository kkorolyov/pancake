package dev.kkorolyov.pancake.editor.ext

import imgui.type.ImBoolean
import imgui.type.ImDouble
import imgui.type.ImInt

/**
 * Returns an `imgui` pointer initialized to [this] value.
 */
fun Boolean.ptr(): ImBoolean = ImBoolean(this)
/**
 * Returns an `imgui` pointer initialized to [this] value.
 */
fun Int.ptr(): ImInt = ImInt(this)
/**
 * Returns an `imgui` pointer initialized to [this] value.
 */
fun Double.ptr(): ImDouble = ImDouble(this)
