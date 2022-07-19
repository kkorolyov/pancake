package dev.kkorolyov.pancake.editor

import imgui.type.ImBoolean

/**
 * Returns a pointer to [this] value.
 */
fun Boolean.ptr(): ImBoolean = ImBoolean(this)
