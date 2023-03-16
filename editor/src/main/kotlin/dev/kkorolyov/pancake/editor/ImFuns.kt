package dev.kkorolyov.pancake.editor

import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.math.Vector3
import imgui.ImGui
import imgui.flag.ImGuiInputTextFlags
import imgui.flag.ImGuiTableFlags
import imgui.type.ImBoolean
import imgui.type.ImDouble

// reuse the same carrier to all imgui input functions
// public only to allow inline functions
/** NO TOUCHY */
val tDouble = ThreadLocal.withInitial(::ImDouble)
/** NO TOUCHY */
val tBoolean = ThreadLocal.withInitial { ImBoolean(false) }
/** NO TOUCHY */
val tDouble2 = ThreadLocal.withInitial(Vector2::of)
/** NO TOUCHY */
val tDouble3 = ThreadLocal.withInitial(Vector3::of)

/**
 * Draws [value] as text.
 */
fun text(value: Any?) {
	ImGui.text(value.toString())
}

/**
 * Draws [value] as text in a tooltip when the last set item is hovered.
 */
fun tooltip(value: Any?) {
	if (ImGui.isItemHovered()) ImGui.setTooltip(value.toString())
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
 * Runs [op] within a group.
 */
inline fun group(op: Op) {
	ImGui.beginGroup()
	op()
	ImGui.endGroup()
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
 * Runs [op] in a tab bar labeled [label].
 */
inline fun tabBar(label: String, op: Op) {
	if (ImGui.beginTabBar(label)) {
		op()
		ImGui.endTabBar()
	}
}
/**
 * Runs [op] in a tab item labeled [label].
 */
inline fun tabItem(label: String, op: Op) {
	if (ImGui.beginTabItem(label)) {
		op()
		ImGui.endTabItem()
	}
}

/**
 * Runs [op] in a table with [id], [flags] and [columns].
 */
inline fun table(id: String, columns: Int, flags: Int = ImGuiTableFlags.None, op: Op) {
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
 * Draws the next item on the same line as the previous one.
 */
fun sameLine() {
	ImGui.sameLine()
}

/**
 * Draws a button with [label], invoking [onPress] when the button is pressed.
 */
inline fun button(label: String, onPress: Op = {}) {
	if (ImGui.button(label)) onPress()
}

/**
 * Draws a checkbox with [label], for [value], invoking [onChange] with the updated value if changed.
 */
inline fun input(label: String, value: Boolean, onChange: OnChange<Boolean>) {
	val ptr = tBoolean.get()
	ptr.set(value)

	if (ImGui.checkbox(label, ptr)) onChange(ptr.get())
}
/**
 * Draws an input field with [label], for [value] with [format] specifier, [step] and [stepFast] step amounts, and input [flags], invoking [onChange] with the updated value if changed.
 */
inline fun input(label: String, value: Double, format: String = "%.3f", step: Double = 0.0, stepFast: Double = 0.0, flags: Int = ImGuiInputTextFlags.None, onChange: OnChange<Double> = {}) {
	val ptr = tDouble.get()
	ptr.set(value)

	if (ImGui.inputDouble(label, ptr, step, stepFast, format, flags)) onChange(ptr.get())
}
/**
 * Draws a multi-segment input field for [value], invoking [onChange] with the updated value if changed.
 * @see input
 */
inline fun input2(label: String, value: Vector2, format: String = "%.3f", step: Double = 0.0, stepFast: Double = 0.0, flags: Int = ImGuiInputTextFlags.None, onChange: OnChange<Vector2> = {}) {
	var changed = false
	val ptr = tDouble2.get()
	ptr.set(value)

	ImGui.beginGroup()
	ImGui.pushItemWidth(((ImGui.getContentRegionAvailX() - ImGui.getStyle().framePaddingX - ImGui.getStyle().windowPaddingX) / 2) - (ImGui.getStyle().itemSpacingX / 2 - 2))

	input("${label}.x", ptr.x, format, step, stepFast, flags) {
		ptr.x = it
		changed = true
	}
	ImGui.sameLine()
	input("${label}.y", ptr.y, format, step, stepFast, flags) {
		ptr.y = it
		changed = true
	}

	ImGui.popItemWidth()
	ImGui.endGroup()

	if (changed) onChange(ptr)
}
/**
 * Draws a multi-segment input field for [value], invoking [onChange] with the updated value if changed.
 * @see input
 */
inline fun input3(label: String, value: Vector3, format: String = "%.3f", step: Double = 0.0, stepFast: Double = 0.0, flags: Int = ImGuiInputTextFlags.None, onChange: OnChange<Vector3> = {}) {
	var changed = false
	val ptr = tDouble3.get()
	ptr.set(value)

	ImGui.beginGroup()
	ImGui.pushItemWidth(((ImGui.getContentRegionAvailX() - ImGui.getStyle().framePaddingX - ImGui.getStyle().windowPaddingX) / 3) - (ImGui.getStyle().itemSpacingX / 3 - 2))

	input("${label}.x", ptr.x, format, step, stepFast, flags) {
		ptr.x = it
		changed = true
	}
	ImGui.sameLine()
	input("${label}.y", ptr.y, format, step, stepFast, flags) {
		ptr.y = it
		changed = true
	}
	ImGui.sameLine()
	input("${label}.z", ptr.z, format, step, stepFast, flags) {
		ptr.z = it
		changed = true
	}

	ImGui.popItemWidth()
	ImGui.endGroup()

	if (changed) onChange(ptr)
}

/**
 * Runs [op] whenever the last item is focused.
 */
inline fun onFocus(op: Op) {
	if (ImGui.isItemFocused()) op()
}

/**
 * Runs [op] when [key] is pressed.
 */
inline fun onKey(key: Int, op: Op) {
	if (ImGui.isKeyPressed(key, false)) op()
}

/**
 * Arbitrary operation.
 */
typealias Op = () -> Unit
/**
 * Invoked with a changed `T` value.
 */
typealias OnChange<T> = (T) -> Unit
