package dev.kkorolyov.pancake.editor.ext

import dev.kkorolyov.pancake.editor.inputs
import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.math.Vector3
import imgui.flag.ImGuiInputTextFlags

/**
 * Renders an input for [this] at the current ImGui stack frame.
 */
fun Vector2.input() {
	input(true)
}
/**
 * Renders a readonly representation of [this] at the current ImGui stack frame.
 */
fun Vector2.readonly() {
	input(false)
}

private fun Vector2.input(writable: Boolean) {
	val ptr = doubleArrayOf(x, y)
	inputs("value", ptr, 0.0, 0.0, "%.3f", if (writable) ImGuiInputTextFlags.None else ImGuiInputTextFlags.ReadOnly)
	if (writable) {
		x = ptr[0]
		y = ptr[1]
	}
}

/**
 * Renders an input for [this] at the current ImGui stack frame.
 */
fun Vector3.input() {
	input(true)
}
/**
 * Renders a readonly representation of [this] at the current ImGui stack frame.
 */
fun Vector3.readonly() {
	input(false)
}

private fun Vector3.input(writable: Boolean) {
	val ptr = doubleArrayOf(x, y, z)
	inputs("value", ptr, 0.0, 0.0, "%.3f", if (writable) ImGuiInputTextFlags.None else ImGuiInputTextFlags.ReadOnly)
	if (writable) {
		x = ptr[0]
		y = ptr[1]
		z = ptr[2]
	}
}
