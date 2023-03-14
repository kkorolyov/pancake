package dev.kkorolyov.pancake.editor.ext

import dev.kkorolyov.pancake.editor.inputs
import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.math.Vector3
import imgui.flag.ImGuiInputTextFlags

/**
 * Renders an input for [this] at the current ImGui stack frame.
 */
fun Vector2.input(label: String) {
	input(label, true)
}
/**
 * Renders a readonly representation of [this] at the current ImGui stack frame.
 */
fun Vector2.readonly(label: String) {
	input(label, false)
}

private fun Vector2.input(label: String, writable: Boolean) {
	val ptr = doubleArrayOf(x, y)
	if (inputs(label, ptr, 0.0, 0.0, "%.3f", if (writable) ImGuiInputTextFlags.None else ImGuiInputTextFlags.ReadOnly)) {
		x = ptr[0]
		y = ptr[1]
	}
}

/**
 * Renders an input for [this] at the current ImGui stack frame.
 */
fun Vector3.input(label: String) {
	input(label, true)
}
/**
 * Renders a readonly representation of [this] at the current ImGui stack frame.
 */
fun Vector3.readonly(label: String) {
	input(label, false)
}

private fun Vector3.input(label: String, writable: Boolean) {
	val ptr = doubleArrayOf(x, y, z)
	if (inputs(label, ptr, 0.0, 0.0, "%.3f", if (writable) ImGuiInputTextFlags.None else ImGuiInputTextFlags.ReadOnly)) {
		x = ptr[0]
		y = ptr[1]
		z = ptr[2]
	}
}
