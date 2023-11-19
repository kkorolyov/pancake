package dev.kkorolyov.pancake.editor

import dev.kkorolyov.pancake.editor.widget.Window
import dev.kkorolyov.pancake.graphics.resource.Texture
import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.math.Vector3
import imgui.flag.ImGuiComboFlags
import imgui.flag.ImGuiDir
import imgui.flag.ImGuiHoveredFlags
import imgui.flag.ImGuiInputTextFlags
import imgui.flag.ImGuiPopupFlags
import imgui.flag.ImGuiSelectableFlags
import imgui.flag.ImGuiTableFlags
import imgui.flag.ImGuiWindowFlags
import imgui.internal.ImGui
import imgui.internal.flag.ImGuiDockNodeFlags
import imgui.type.ImBoolean
import imgui.type.ImDouble
import imgui.type.ImInt
import imgui.type.ImString

// reuse the same carrier to all imgui input functions
// public only to allow inline functions
/** NO TOUCHY */
val tString by ThreadLocal.withInitial(::ImString)
/** NO TOUCHY */
val tBoolean by ThreadLocal.withInitial(::ImBoolean)
/** NO TOUCHY */
val tInt by ThreadLocal.withInitial(::ImInt)
/** NO TOUCHY */
val tDouble by ThreadLocal.withInitial(::ImDouble)
/** NO TOUCHY */
val tDouble2 by ThreadLocal.withInitial(Vector2::of)
/** NO TOUCHY */
val tDouble3 by ThreadLocal.withInitial(Vector3::of)

/**
 * Initializes [id] dock space with [setup], if it does not yet exist.
 * Otherwise, simply draws the current [id] dock space.
 */
inline fun dockSpace(id: String, setup: Op) {
	val nodeId = ImGui.getID(id)

	if (ImGui.dockBuilderGetNode(nodeId).isNotValidPtr) {
		ImGui.dockBuilderAddNode(nodeId, ImGuiDockNodeFlags.DockSpace)
		ImGui.dockBuilderSetNodeSize(nodeId, ImGui.getContentRegionAvailX(), ImGui.getContentRegionAvailY())

		setup()
	}

	ImGui.dockSpace(nodeId)
}
/**
 * Adds [window] to [id] dock space.
 * Throws [IllegalArgumentException] if [id] dock space does not exist.
 */
fun dock(id: String, window: Window) {
	val nodeId = ImGui.getID(id)
	if (ImGui.dockBuilderGetNode(nodeId).isNotValidPtr) throw IllegalArgumentException("no such dock space [$id]")

	ImGui.dockBuilderDockWindow(window.label, nodeId)
	ImGui.dockBuilderFinish(nodeId)
}
/**
 * Adds [other] to the same dock node as [window].
 * If [window] is not currently docked, creates a new floating dock space encompassing both [window] and [other].
 */
fun dock(window: Window, other: Window) {
	ImGui.begin(window.label)
	val nodePtr = ImGui.dockBuilderGetNode(ImGui.getWindowDockID())
	ImGui.end()

	val nodeId = if (nodePtr.isValidPtr) nodePtr.id else ImGui.dockBuilderAddNode()
	if (nodePtr.isNotValidPtr) ImGui.dockBuilderDockWindow(window.label, nodeId)
	ImGui.dockBuilderDockWindow(other.label, nodeId)

	ImGui.dockBuilderFinish(nodeId)
}
/**
 * Splits [window] dock node to accommodate [other] in [direction] with [ratio] of original space.
 * If [window] is not currently docked, creates a new floating dock space encompassing both [window] and [other].
 */
fun dock(window: Window, other: Window, direction: Int = ImGuiDir.None, ratio: Float) {
	ImGui.begin(window.label)
	val nodePtr = ImGui.dockBuilderGetNode(ImGui.getWindowDockID())
	ImGui.end()

	val nodeIdPtr = ImInt(if (nodePtr.isValidPtr) nodePtr.id else ImGui.dockBuilderAddNode())

	val otherId = ImGui.dockBuilderSplitNode(nodeIdPtr.get(), direction, ratio, null, nodeIdPtr)

	ImGui.dockBuilderDockWindow(window.label, nodeIdPtr.get())
	ImGui.dockBuilderDockWindow(other.label, otherId)

	ImGui.dockBuilderFinish(nodeIdPtr.get())
}

/**
 * Draws [value] as text.
 */
fun text(value: Any) {
	ImGui.text(value.toString())
}

/**
 * Draws [value] as text in a tooltip when the last set item is hovered.
 */
fun tooltip(value: Any) {
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
 * Runs [op] in a context menu popup over the last clicked item.
 * Accepts a manual item [id] to bind to.
 */
inline fun contextMenu(id: String? = null, flags: Int = ImGuiPopupFlags.MouseButtonRight, op: Op) {
	if (id?.let { ImGui.beginPopupContextItem(it, flags) } ?: ImGui.beginPopupContextItem(flags)) {
		op()
		ImGui.endPopup()
	}
}

/**
 * Runs [op] when the last set item is clicked.
 */
inline fun onClick(op: Op) {
	if (ImGui.isItemClicked()) op()
}
/**
 * Runs [op] when the last set item is hovered.
 */
inline fun onHover(flags: Int = ImGuiHoveredFlags.None, op: Op) {
	if (ImGui.isItemHovered(flags)) op()
}
/**
 * Runs [op] while the last set item is active (e.g. held down, being edited).
 */
inline fun onActive(op: Op) {
	if (ImGui.isItemActive()) op()
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
 * Runs [op] within an embedded region named [id].
 */
inline fun child(id: String, width: Float = 0.0f, height: Float = 0.0f, border: Boolean = false, flags: Int = ImGuiWindowFlags.None, op: Op) {
	if (ImGui.beginChild(id, width, height, border, flags)) op()
	ImGui.endChild()
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
inline fun list(label: String, stretch: Boolean = false, op: Op) {
	if (ImGui.beginListBox(label, if (label.startsWith("##")) -1.0f else 0.0f, if (stretch) -1.0f else 0.0f)) {
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
 * Invokes [op] within a menu of [label].
 */
inline fun menu(label: String, op: Op) {
	if (ImGui.beginMenu(label)) {
		op()
		ImGui.endMenu()
	}
}
/**
 * Draws a menu item of [label], invoking [onClick] when it is selected.
 * Returns `true` when selected.
 */
inline fun menuItem(label: String, onClick: Op): Boolean {
	val result = ImGui.menuItem(label)
	if (result) onClick()
	return result
}

/**
 * Draws the next item on the same line as the previous one.
 */
fun sameLine() {
	ImGui.sameLine()
}

/**
 * Draws a separator line.
 */
fun separator() {
	ImGui.separator()
}

/**
 * Draws [texture] with [width] and [height].
 * Optionally [flip]s the image vertically when rendering.
 */
fun image(texture: Texture, width: Float, height: Float, flip: Boolean = false) {
	if (flip) ImGui.image(texture.id, width, height, 0.0f, 1.0f, 1.0f, 0.0f)
	else ImGui.image(texture.id, width, height)
}

/**
 * Runs [op] within an interaction-disabled scope if [disabled] is `true`.
 * Otherwise, just runs [op].
 */
inline fun disabledIf(disabled: Boolean, op: Op) {
	if (disabled) ImGui.beginDisabled()
	op()
	if (disabled) ImGui.endDisabled()
}

/**
 * Draws a selectable area for [value] as text, using [flags], invoking [onClick] when it is selected.
 * Returns `true` when selected.
 */
inline fun selectable(value: Any, flags: Int = ImGuiSelectableFlags.None, onClick: Op): Boolean {
	val result = ImGui.selectable(value.toString(), false, flags)
	if (result) onClick()
	return result
}

/**
 * Draws a button with [label], invoking [onClick] when the button is pressed.
 * Returns `true` when pressed.
 */
inline fun button(label: String, onClick: Op): Boolean {
	val result = ImGui.button(label)
	if (result) onClick()
	return result
}

/**
 * Draws an input field with [label], for [value], and input [flags], invoking [onChange] with the updated value if changed.
 * Returns `true` when changed.
 */
inline fun input(label: String, value: String, flags: Int = ImGuiInputTextFlags.None, onChange: OnChange<String>): Boolean {
	val ptr = tString
	ptr.set(value)

	val result = ImGui.inputText(label, ptr, flags)
	if (result) onChange(ptr.get())
	return result
}
/**
 * Draws an input field with [label], for [value] as a dropdown of all [T] values, and input [flags], invoking [onChange] with the updated value if changed.
 * Returns `true` when changed.
 */
inline fun <reified T : Enum<T>> input(label: String, value: T, flags: Int = ImGuiComboFlags.None, onChange: OnChange<T>): Boolean {
	var result: T? = null

	if (ImGui.beginCombo(label, value.name, flags)) {
		for (enumValue in enumValues<T>()) {
			if (enumValue == value) {
				selectable(enumValue.name) { }
				ImGui.setItemDefaultFocus()
			} else {
				selectable(enumValue.name) { result = enumValue }
			}
		}

		ImGui.endCombo()
	}

	result?.let(onChange)
	return result != null
}
/**
 * Draws a checkbox with [label], for [value], invoking [onChange] with the updated value if changed.
 * Returns `true` when changed.
 */
inline fun input(label: String, value: Boolean, onChange: OnChange<Boolean>): Boolean {
	val ptr = tBoolean
	ptr.set(value)

	val result = ImGui.checkbox(label, ptr)
	if (result) onChange(ptr.get())
	return result
}
/**
 * Draws an input field with [label], for [value], [step] and [stepFast] step amounts, and input [flags], invoking [onChange] with the updated value if changed.
 * Returns `true` when changed.
 */
inline fun input(label: String, value: Int, step: Int = 0, stepFast: Int = 0, flags: Int = ImGuiInputTextFlags.None, onChange: OnChange<Int> = {}): Boolean {
	val ptr = tInt
	ptr.set(value)

	val result = stretch(label) { ImGui.inputInt(label, ptr, step, stepFast, flags) }
	if (result) onChange(ptr.get())
	return result
}
/**
 * Draws an input field with [label], for [value] with [format] specifier, [step] and [stepFast] step amounts, and input [flags], invoking [onChange] with the updated value if changed.
 * Returns `true` when changed.
 */
inline fun input(label: String, value: Double, format: String = "%.3f", step: Double = 0.0, stepFast: Double = 0.0, flags: Int = ImGuiInputTextFlags.None, onChange: OnChange<Double> = {}): Boolean {
	val ptr = tDouble
	ptr.set(value)

	val result = stretch(label) { ImGui.inputDouble(label, ptr, step, stepFast, format, flags) }
	if (result) onChange(ptr.get())
	return result
}
/**
 * Draws a multi-segment input field for [value], invoking [onChange] with the updated value if changed.
 * Returns `true` when changed.
 * @see input
 */
inline fun input2(label: String, value: Vector2, format: String = "%.3f", step: Double = 0.0, stepFast: Double = 0.0, flags: Int = ImGuiInputTextFlags.None, onChange: OnChange<Vector2> = {}): Boolean {
	var changed = false
	val ptr = tDouble2
	ptr.set(value)

	table(label, 2, ImGuiTableFlags.SizingStretchSame) {
		column {
			input("${label}.x", ptr.x, format, step, stepFast, flags) {
				ptr.x = it
				changed = true
			}
		}
		column {
			input("${label}.y", ptr.y, format, step, stepFast, flags) {
				ptr.y = it
				changed = true
			}
		}
	}

	if (changed) onChange(ptr)
	return changed
}
/**
 * Draws a multi-segment input field for [value], invoking [onChange] with the updated value if changed.
 * Returns `true` when changed.
 * @see input
 */
inline fun input3(label: String, value: Vector3, format: String = "%.3f", step: Double = 0.0, stepFast: Double = 0.0, flags: Int = ImGuiInputTextFlags.None, onChange: OnChange<Vector3> = {}): Boolean {
	var changed = false
	val ptr = tDouble3
	ptr.set(value)

	group {
		table(label, 3, ImGuiTableFlags.SizingStretchSame) {
			column {
				input("${label}.x", ptr.x, format, step, stepFast, flags) {
					ptr.x = it
					changed = true
				}
			}
			column {
				input("${label}.y", ptr.y, format, step, stepFast, flags) {
					ptr.y = it
					changed = true
				}
			}
			column {
				input("${label}.z", ptr.z, format, step, stepFast, flags) {
					ptr.z = it
					changed = true
				}
			}
		}
	}

	if (changed) onChange(ptr)
	return changed
}

/**
 * Runs [op] whenever the last item is focused.
 * Returns `true` when focused.
 */
inline fun onFocus(op: Op): Boolean {
	val result = ImGui.isItemFocused()
	if (result) op()
	return result
}

/**
 * Runs [op] when [key] is pressed.
 * Returns `true` when pressed.
 */
inline fun onKey(key: Int, op: Op): Boolean {
	val result = ImGui.isKeyPressed(key, false)
	if (result) op()
	return result
}

/**
 * Runs [op] when mouse [button] is double-click.
 * Returns `true` when double-clicked.
 */
inline fun onDoubleClick(button: Int, op: Op): Boolean {
	val result = ImGui.isMouseDoubleClicked(button)
	if (result) op()
	return result
}

/**
 * Runs [op] within a width region calculated from its [label] and returns its result.
 */
inline fun <T> stretch(label: String, op: () -> T): T {
	val result: T

	if (label.startsWith("##")) {
		ImGui.pushItemWidth(-1.0f)
		result = op()
		ImGui.popItemWidth()
	} else {
		result = op()
	}

	return result
}

/**
 * Arbitrary operation.
 */
typealias Op = () -> Unit
/**
 * Invoked with a changed `T` value.
 */
typealias OnChange<T> = (T) -> Unit
