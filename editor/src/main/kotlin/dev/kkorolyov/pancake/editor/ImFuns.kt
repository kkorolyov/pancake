package dev.kkorolyov.pancake.editor

import dev.kkorolyov.pancake.editor.ext.ptr
import imgui.ImGui
import imgui.flag.ImGuiInputTextFlags
import imgui.flag.ImGuiTableFlags

private typealias Op = () -> Unit

/**
 * Draws [value] as text.
 */
fun text(value: Any) {
	ImGui.text(value.toString())
}

/**
 * Draws tooltip [text] when the last set item is hovered.
 */
fun tooltip(text: String) {
	if (ImGui.isItemHovered()) ImGui.setTooltip(text)
}
/**
 * Runs [op] in a tooltip when the last set item is hovered.
 */
inline fun tooltip(op: Op) {
	if (ImGui.isItemHovered()) {
		ImGui.beginTooltip()
		op()
		ImGui.endTooltip()
	}
}

/**
 * Runs [op] when the last set item is clicked.
 */
inline fun onClick(op: Op) {
	if (ImGui.isItemClicked()) op()
}

/**
 * Runs [op] in a tree node labeled [label].
 */
inline fun tree(label: String, op: Op) {
	if (ImGui.treeNode(label)) {
		op()
		ImGui.treePop()
	}
}

/**
 * Runs [op] in a header labeled [label].
 */
inline fun header(label: String, op: Op) {
	if (ImGui.collapsingHeader(label)) op()
}

/**
 * Runs [op] in a table with [id], [flags] and [columns].
 */
inline fun table(id: String, columns: Int, flags: Int = ImGuiTableFlags.Reorderable or ImGuiTableFlags.Resizable, op: Op) {
	if (ImGui.beginTable(id, columns, flags)) {
		op()
		ImGui.endTable()
	}
}
/**
 * Runs [op] in a new table column.
 */
inline fun column(op: Op) {
	if (ImGui.tableNextColumn()) op()
}

/**
 * Runs [op] in a list box labeled [label].
 */
inline fun list(label: String, op: Op) {
	if (ImGui.beginListBox(label)) {
		op()
		ImGui.endListBox()
	}
}

/**
 * Runs [op] in an indented level.
 */
inline fun indented(op: Op) {
	ImGui.indent()
	op()
	ImGui.unindent()
}

/**
 * Draws a multi-segment input for [values].
 * Mutates [values] with the current corresponding input values.
 */
fun inputs(id: String, values: DoubleArray, step: Double, stepMax: Double, format: String, flags: Int = ImGuiInputTextFlags.None) {
	ImGui.beginGroup()
	ImGui.pushItemWidth(((ImGui.getContentRegionAvailX() - ImGui.getStyle().framePaddingX - ImGui.getStyle().windowPaddingX) / values.size) - (ImGui.getStyle().itemSpacingX / values.size - 2))
	values.forEachIndexed { i, value ->
		val ptr = value.ptr()
		ImGui.inputDouble("##${id}${i}", ptr, step, stepMax, format, flags)
		values[i] = ptr.get()

		if (i < values.size - 1) ImGui.sameLine()
	}
	ImGui.popItemWidth()
	ImGui.endGroup()
}
