package dev.kkorolyov.pancake.editor

import dev.kkorolyov.pancake.editor.widget.Window
import dev.kkorolyov.pancake.graphics.resource.Texture
import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.math.Vector3
import imgui.ImColor
import imgui.ImDrawList
import imgui.ImFont
import imgui.ImGuiStyle
import imgui.ImGuiViewport
import imgui.ImVec2
import imgui.ImVec4
import imgui.extension.implot.ImPlot
import imgui.extension.implot.flag.ImPlotAxis
import imgui.extension.implot.flag.ImPlotAxisFlags
import imgui.extension.implot.flag.ImPlotCond
import imgui.extension.implot.flag.ImPlotDragToolFlags
import imgui.extension.implot.flag.ImPlotFlags
import imgui.flag.ImGuiCol
import imgui.flag.ImGuiComboFlags
import imgui.flag.ImGuiCond
import imgui.flag.ImGuiDir
import imgui.flag.ImGuiDragDropFlags
import imgui.flag.ImGuiHoveredFlags
import imgui.flag.ImGuiInputTextFlags
import imgui.flag.ImGuiMouseButton
import imgui.flag.ImGuiPopupFlags
import imgui.flag.ImGuiSelectableFlags
import imgui.flag.ImGuiSliderFlags
import imgui.flag.ImGuiTableColumnFlags
import imgui.flag.ImGuiTableFlags
import imgui.flag.ImGuiTreeNodeFlags
import imgui.flag.ImGuiWindowFlags
import imgui.internal.ImGui
import imgui.internal.flag.ImGuiDockNodeFlags
import imgui.type.ImBoolean
import imgui.type.ImDouble
import imgui.type.ImInt
import imgui.type.ImString
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

// reuse the same carrier to all imgui input functions
private val tString by ThreadLocal.withInitial(::ImString)
private val tBoolean by ThreadLocal.withInitial(::ImBoolean)
private val tInt by ThreadLocal.withInitial(::ImInt)
private val tFloat2 by ThreadLocal.withInitial { FloatArray(2) }
private val tFloat3 by ThreadLocal.withInitial { FloatArray(3) }
private val tDouble by ThreadLocal.withInitial(::ImDouble)
private val tDouble1 by ThreadLocal.withInitial(::ImDouble)
private val tVector2 by ThreadLocal.withInitial(Vector2::of)
private val tVector3 by ThreadLocal.withInitial(Vector3::of)
private val tVec2 by ThreadLocal.withInitial(::ImVec2)
private val tVec4 by ThreadLocal.withInitial(::ImVec4)

/**
 * Returns a shared thread-local pointer instance for [value].
 */
fun pointer(value: String): ImString = tString.apply { set(value) }
/**
 * Returns a shared thread-local pointer instance for [value].
 */
fun pointer(value: Boolean): ImBoolean = tBoolean.apply { set(value) }
/**
 * Returns a shared thread-local pointer instance for [value].
 */
fun pointer(value: Int): ImInt = tInt.apply { set(value) }
/**
 * Returns a shared thread-local pointer instance for [value].
 */
fun pointer(value: Double): ImDouble = tDouble.apply { set(value) }
/**
 * Returns a shared thread-local pointer instance for [value].
 */
fun pointer1(value: Double): ImDouble = tDouble1.apply { set(value) }
/**
 * Returns a shared thread-local pointer instance for the ([value0], [value1]) vector.
 */
fun pointer(value0: Float, value1: Float): ImVec2 = tVec2.apply { set(value0, value1) }
/**
 * Returns a shared thread-local pointer instance for the ([value0], [value1], [value2], [value3]) vector.
 */
fun pointer(value0: Float, value1: Float, value2: Float, value3: Float): ImVec4 = tVec4.apply { set(value0, value1, value2, value3) }
@Deprecated("replace usages with vec2")
fun pointerArr(value0: Float, value1: Float): FloatArray = tFloat2.apply {
	set(0, value0)
	set(1, value1)
}
@Deprecated("replace usages with vec3")
fun pointerArr(value0: Float, value1: Float, value2: Float): FloatArray = tFloat3.apply {
	set(0, value0)
	set(1, value1)
	set(2, value2)
}

@Deprecated("these are weird - replace with primitives")
fun pointerVec(x: Double, y: Double): Vector2 = tVector2.apply {
	this.x = x
	this.y = y
}
@Deprecated("these are weird - replace with primitives")
fun pointerVec(x: Double, y: Double, z: Double): Vector3 = tVector3.apply {
	this.x = x
	this.y = y
	this.z = z
}

/**
 * Initializes [id] dock space with [setup], if it does not yet exist.
 * Otherwise, simply draws the current [id] dock space.
 */
inline fun dockSpace(id: String, setup: Ctx.DockSpace.() -> Unit) {
	val nodeId = ImGui.getID(id)

	if (ImGui.dockBuilderGetNode(nodeId).isNotValidPtr) {
		ImGui.dockBuilderAddNode(nodeId, ImGuiDockNodeFlags.DockSpace)
		ImGui.dockBuilderSetNodeSize(nodeId, ImGui.getContentRegionAvailX(), ImGui.getContentRegionAvailY())

		Ctx.DockSpace.setup()
	}

	ImGui.dockSpace(nodeId)
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
	tooltip(ImGuiHoveredFlags.None, value)
}
/**
 * Draws [value] as text in a tooltip when the last set item is hovered.
 */
fun tooltip(flags: Int, value: Any) {
	if (ImGui.isItemHovered(flags)) ImGui.setTooltip(value.toString())
}
/**
 * Runs [op] in a tooltip when the last set item is hovered or [force] is `true`.
 */
inline fun tooltip(flags: Int = ImGuiHoveredFlags.None, force: Boolean = false, op: Op) {
	if (ImGui.isItemHovered(flags) || force) {
		ImGui.beginTooltip()
		op()
		ImGui.endTooltip()
	}
}

/**
 * Runs [op] in a context menu popup over the last clicked item.
 * Accepts a manual item [id] to bind to.
 */
inline fun contextMenu(id: String? = null, flags: Int = ImGuiPopupFlags.MouseButtonRight, op: Ctx.Menu.() -> Unit) {
	if (id?.let { ImGui.beginPopupContextItem(it, flags) } ?: ImGui.beginPopupContextItem(flags)) {
		Ctx.Menu.op()
		ImGui.endPopup()
	}
}

/**
 * Runs [op] and returns `true` when the last set item is clicked.
 */
inline fun onClick(button: Int = ImGuiMouseButton.Left, op: Op): Boolean {
	val result = ImGui.isItemClicked(button)
	if (result) op()
	return result
}
/**
 * Runs [op] and returns `true` while the last set item is hovered.
 */
inline fun onHover(flags: Int = ImGuiHoveredFlags.None, op: Op = {}): Boolean {
	val result = ImGui.isItemHovered(flags)
	if (result) op()
	return result
}

/**
 * Runs [op] while the last set item is active (e.g. held down, being edited).
 */
inline fun onActive(op: Op) {
	if (ImGui.isItemActive()) op()
}
/**
 * Runs [onStart] when the last set item is activated, and [onEnd] when it is deactivated.
 */
inline fun onActive(onStart: Op, onEnd: Op) {
	if (ImGui.isItemActivated()) onStart()
	if (ImGui.isItemDeactivated()) onEnd()
}

/**
 * Runs [op] while the last item is focused.
 */
inline fun onFocus(op: Op) {
	if (ImGui.isItemFocused()) op()
}

/**
 * Runs [op] while the last set item is dragged.
 * See also: [onDrop]
 */
inline fun onDrag(flags: Int = ImGuiDragDropFlags.None, op: Ctx.Drag.() -> Unit) {
	if (ImGui.beginDragDropSource(flags)) {
		Ctx.Drag.op()
		ImGui.endDragDropSource()
	}
}
/**
 * Runs [op] when a dragged payload is dropped on the last set item.
 * See also: [onDrag]
 */
inline fun onDrop(op: Ctx.Drop.() -> Unit) {
	if (ImGui.beginDragDropTarget()) {
		Ctx.Drop.op()
		ImGui.endDragDropTarget()
	}
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
inline fun child(id: String, width: Float = 0f, height: Float = 0f, border: Boolean = false, flags: Int = ImGuiWindowFlags.None, op: Op) {
	if (ImGui.beginChild(id, width, height, border, flags)) op()
	ImGui.endChild()
}

/**
 * Runs [op] in a tree node labeled [label].
 * Returns `true` when the node is open.
 */
inline fun tree(label: String, flags: Int = ImGuiTreeNodeFlags.None, op: Op): Boolean {
	val result = ImGui.treeNodeEx(label, flags)
	if (result) {
		op()
		ImGui.treePop()
	}
	return result
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
inline fun table(id: String, columns: Int, width: Float = 0f, height: Float = 0f, flags: Int = ImGuiTableFlags.None, op: Ctx.Table.() -> Unit) {
	if (ImGui.beginTable(id, columns, flags, width, height)) {
		Ctx.Table.op()
		ImGui.endTable()
	}
}

/**
 * Runs [op] in a list box labeled [label].
 */
inline fun list(label: String, width: Float = 0f, height: Float = 0f, op: Op) {
	if (ImGui.beginListBox(label, width, height)) {
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
inline fun menu(label: String, op: Ctx.Menu.() -> Unit) {
	if (ImGui.beginMenu(label)) {
		Ctx.Menu.op()
		ImGui.endMenu()
	}
}

/**
 * Invokes [op] within a plot of [label].
 */
inline fun plot(label: String, width: Float = 0f, height: Float = 0f, flags: Int = ImPlotFlags.None, op: Ctx.Plot.() -> Unit) {
	if (ImPlot.beginPlot(label, width, height, flags)) {
		Ctx.Plot.op()
		ImPlot.endPlot()
	}
}
/**
 * Invokes [op] within a [rows] x [columns] subplots in a plot space of [label].
 * Each [plot] call configures the next subplot in row-major order.
 */
inline fun subplots(label: String, rows: Int, columns: Int, width: Float = 0f, height: Float = 0f, flags: Int = ImPlotFlags.None, op: () -> Unit) {
	if (ImPlot.beginSubplots(label, rows, columns, width, height, flags)) {
		op()
		ImPlot.endSubplots()
	}
}

/**
 * Reserves a [width] x [height] space at the current cursor position.
 * Optionally binds the area to [id], capturing mouse interactions.
 * Intended for use with [Draw] API.
 */
fun dummy(width: Float, height: Float, id: String? = null) {
	val ptr = pointer(width, height)
	id?.let { ImGui.invisibleButton(id, ptr) } ?: ImGui.dummy(ptr)
}

/**
 * Draws the next item on the same line as the previous one.
 */
fun sameLine() {
	ImGui.sameLine()
}
/**
 * Undoes a [sameLine].
 */
fun newLine() {
	ImGui.newLine()
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
	if (flip) ImGui.image(texture.id.toLong(), width, height, 0f, 1f, 1f, 0f)
	else ImGui.image(texture.id.toLong(), width, height)
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
 * Draws a selectable area for [value] as text, using [selected] and [flags], invoking [onClick] when it is selected.
 * Returns `true` when selected.
 */
inline fun selectable(value: Any, selected: Boolean = false, flags: Int = ImGuiSelectableFlags.None, onClick: Op = {}): Boolean {
	val result = ImGui.selectable(value.toString(), selected, flags)
	if (result) onClick()
	return result
}

/**
 * Draws a combo box with [label], supported [options], current [value], and [flags], invoking [onChange] with the updated value if changed.
 * Returns `true` when changed.
 */
inline fun <T> combo(label: String, options: Iterable<T>, value: T, flags: Int = ImGuiComboFlags.None, onChange: OnChange<T>): Boolean {
	var result: T? = null

	if (ImGui.beginCombo(label, value.toString(), flags)) {
		options.forEach {
			if (it == value) {
				selectable(it.toString()) { }
				ImGui.setItemDefaultFocus()
			} else {
				selectable(it.toString()) { result = it }
			}
		}

		ImGui.endCombo()
	}

	result?.let(onChange)
	return result != null
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
	val ptr = pointer(value)

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
	val ptr = pointer(value)

	val result = ImGui.checkbox(label, ptr)
	if (result) onChange(ptr.get())
	return result
}
/**
 * Draws an input field with [label], for [value], [step] and [stepFast] step amounts, and input [flags], invoking [onChange] with the updated value if changed.
 * Can also be provided [digitWidth] to auto size unlabeled inputs enough to fit [digitWidth] digits without overflow.
 * Returns `true` when changed.
 */
inline fun input(label: String, value: Int, step: Int = 0, stepFast: Int = 0, digitWidth: Int = 3, flags: Int = ImGuiInputTextFlags.None, onChange: OnChange<Int> = {}): Boolean {
	val ptr = pointer(value)

	val width = if (label.startsWith("##")) Layout.textWidth("${10.0.pow(digitWidth)}${if (step != 0 || stepFast != 0) "+++++" else ""}") else null

	width?.let(ImGui::pushItemWidth)
	val result = ImGui.inputInt(label, ptr, step, stepFast, flags)
	width?.let { ImGui.popItemWidth() }

	if (result) onChange(ptr.get())
	return result
}
/**
 * Draws an input field with [label], for [value] with [format] specifier, [step] and [stepFast] step amounts, and input [flags], invoking [onChange] with the updated value if changed.
 * Can also be provided [digitWidth] to auto size unlabeled inputs enough to fit [digitWidth] digits (while considering [format]) without overflow.
 * Returns `true` when changed.
 */
inline fun input(label: String, value: Double, format: String = "%.3f", step: Double = 0.0, stepFast: Double = 0.0, digitWidth: Int = 3, flags: Int = ImGuiInputTextFlags.None, onChange: OnChange<Double> = {}): Boolean {
	val ptr = pointer(value)

	val width = if (label.startsWith("##")) Layout.textWidth("${format.format(10.0.pow(digitWidth))}${if (step != 0.0 || stepFast != 0.0) "+++++" else ""}") else null

	width?.let(ImGui::pushItemWidth)
	val result = ImGui.inputDouble(label, ptr, step, stepFast, format, flags)
	width?.let { ImGui.popItemWidth() }

	if (result) onChange(ptr.get())
	return result
}
/**
 * Draws a multi-segment input field for [value], invoking [onChange] with the updated value if changed.
 * Can also be provided [digitWidth] to auto size unlabeled inputs enough to fit [digitWidth] digits (while considering [format]) without overflow.
 * Returns `true` when changed.
 * @see input
 */
inline fun input2(label: String, value: Vector2, format: String = "%.3f", step: Double = 0.0, stepFast: Double = 0.0, digitWidth: Int = 3, flags: Int = ImGuiInputTextFlags.None, onChange: OnChange<Vector2> = {}): Boolean {
	// TODO replace with calls to fixed ImGui.inputScalarN bindings
	val ptr = pointerArr(value.x.toFloat(), value.y.toFloat())

	val width = if (label.startsWith("##")) Layout.textWidth(format.format(10.0.pow(digitWidth))) * 2 else null
	width?.let(ImGui::pushItemWidth)
	val result = ImGui.inputFloat2(label, ptr, format, flags)
	width?.let { ImGui.popItemWidth() }

	if (result) {
		val returnPtr = pointerVec(ptr[0].toDouble(), ptr[1].toDouble())
		onChange(returnPtr)
	}
	return result
}
/**
 * Draws a multi-segment input field for [value], invoking [onChange] with the updated value if changed.
 * Can also be provided [digitWidth] to auto size unlabeled inputs enough to fit [digitWidth] digits (while considering [format]) without overflow.
 * Returns `true` when changed.
 * @see input
 */
inline fun input3(label: String, value: Vector3, format: String = "%.3f", step: Double = 0.0, stepFast: Double = 0.0, digitWidth: Int = 3, flags: Int = ImGuiInputTextFlags.None, onChange: OnChange<Vector3> = {}): Boolean {
	// TODO replace with calls to fixed ImGui.inputScalarN bindings
	val ptr = pointerArr(value.x.toFloat(), value.y.toFloat(), value.z.toFloat())

	val width = if (label.startsWith("##")) Layout.textWidth(format.format(10.0.pow(digitWidth))) * 3 else null
	width?.let(ImGui::pushItemWidth)
	val result = ImGui.inputFloat3(label, ptr, format, flags)
	width?.let { ImGui.popItemWidth() }

	if (result) {
		val returnPtr = pointerVec(ptr[0].toDouble(), ptr[1].toDouble(), ptr[2].toDouble())
		onChange(returnPtr)
	}
	return result
}

/**
 * Draws a drag input for [value], invoking [onChange] with the updated value if changed.
 * Returns `true` when changed.
 */
inline fun dragInput(label: String, value: Int, format: String = "%d", min: Int = -Float.MAX_VALUE.toInt(), max: Int = Float.MAX_VALUE.toInt(), speed: Float = 0.2f, onChange: OnChange<Int> = { }): Boolean {
	val ptr = intArrayOf(value)

	val result = ImGui.dragInt(label, ptr, speed, min, max, format)
	if (result) onChange(max(min, min(max, ptr[0])))
	return result
}
/**
 * Draws a drag input for [value] and [value1], invoking [onChange] with the updated values if changed.
 * Returns `true` when changed.
 */
inline fun dragInput(label: String, value: Int, value1: Int, format: String = "%d", min: Int = -Float.MAX_VALUE.toInt(), max: Int = Float.MAX_VALUE.toInt(), speed: Float = 0.2f, onChange: OnChange2<Int> = { _, _ -> }): Boolean {
	val ptr = intArrayOf(value, value1)

	val result = ImGui.dragInt2(label, ptr, speed, min, max, format)
	if (result) onChange(max(min, min(max, ptr[0])), max(min, min(max, ptr[1])))
	return result
}
/**
 * Draws a drag input for [value], [value1], and [value2], invoking [onChange] with the updated values if changed.
 * Returns `true` when changed.
 */
inline fun dragInput(label: String, value: Int, value1: Int, value2: Int, format: String = "%d", min: Int = -Float.MAX_VALUE.toInt(), max: Int = Float.MAX_VALUE.toInt(), speed: Float = 0.2f, onChange: OnChange3<Int> = { _, _, _ -> }): Boolean {
	val ptr = intArrayOf(value, value1, value2)

	val result = ImGui.dragInt3(label, ptr, speed, min, max, format)
	if (result) onChange(max(min, min(max, ptr[0])), max(min, min(max, ptr[1])), max(min, min(max, ptr[2])))
	return result
}

/**
 * Draws a drag input for [value], invoking [onChange] with the updated value if changed.
 * Returns `true` when changed.
 */
inline fun dragInput(label: String, value: Double, format: String = "%.3f", min: Double = -Float.MAX_VALUE.toDouble(), max: Double = Float.MAX_VALUE.toDouble(), speed: Float = 0.1f, flags: Int = ImGuiSliderFlags.None, onChange: OnChange<Double> = { }): Boolean {
	val ptr = floatArrayOf(value.toFloat())

	val result = ImGui.dragFloat(label, ptr, speed, min.toFloat(), max.toFloat(), format, flags)
	if (result) onChange(max(min, min(max, ptr[0].toDouble())))
	return result
}
/**
 * Draws a drag input for [value] and [value1], invoking [onChange] with the updated values if changed.
 * Returns `true` when changed.
 */
inline fun dragInput(label: String, value: Double, value1: Double, format: String = "%.3f", min: Double = -Float.MAX_VALUE.toDouble(), max: Double = Float.MAX_VALUE.toDouble(), speed: Float = 0.1f, flags: Int = ImGuiSliderFlags.None, onChange: OnChange2<Double> = { _, _ -> }): Boolean {
	val ptr = floatArrayOf(value.toFloat(), value1.toFloat())

	val result = ImGui.dragFloat2(label, ptr, speed, min.toFloat(), max.toFloat(), format, flags)
	if (result) onChange(max(min, min(max, ptr[0].toDouble())), max(min, min(max, ptr[1].toDouble())))
	return result
}
/**
 * Draws a drag input for [value], [value1], and [value2], invoking [onChange] with the updated values if changed.
 * Returns `true` when changed.
 */
inline fun dragInput(label: String, value: Double, value1: Double, value2: Double, format: String = "%.3f", min: Double = -Float.MAX_VALUE.toDouble(), max: Double = Float.MAX_VALUE.toDouble(), speed: Float = 0.1f, flags: Int = ImGuiSliderFlags.None, onChange: OnChange3<Double> = { _, _, _ -> }): Boolean {
	val ptr = floatArrayOf(value.toFloat(), value1.toFloat(), value2.toFloat())

	val result = ImGui.dragFloat3(label, ptr, speed, min.toFloat(), max.toFloat(), format, flags)
	if (result) onChange(max(min, min(max, ptr[0].toDouble())), max(min, min(max, ptr[1].toDouble())), max(min, min(max, ptr[2].toDouble())))
	return result
}

/**
 * Draws a drag input for [value]'s components, invoking [onChange] with the updated value if changed.
 * Returns `true` when changed.
 */
inline fun dragInput2(label: String, value: Vector2, format: String = "%.3f", min: Double = -Float.MAX_VALUE.toDouble(), max: Double = Float.MAX_VALUE.toDouble(), speed: Float = 0.1f, flags: Int = ImGuiSliderFlags.None, onChange: OnChange<Vector2> = { }): Boolean =
	dragInput(label, value.x, value.y, format, min, max, speed, flags) { x, y -> onChange(Vector2.of(x, y)) }
/**
 * Draws a drag input for [value]'s components, invoking [onChange] with the updated value if changed.
 * Returns `true` when changed.
 */
inline fun dragInput3(label: String, value: Vector3, format: String = "%.3f", min: Double = -Float.MAX_VALUE.toDouble(), max: Double = Float.MAX_VALUE.toDouble(), speed: Float = 0.1f, flags: Int = ImGuiSliderFlags.None, onChange: OnChange<Vector3> = { }): Boolean =
	dragInput(label, value.x, value.y, value.z, format, min, max, speed, flags) { x, y, z -> onChange(Vector3.of(x, y, z)) }

/**
 * Draws a slider input for [value], invoking [onChange] with the updated value if changed.
 * Returns `true` when changed.
 */
inline fun sliderInput(label: String, value: Int, format: String = "%d", min: Int = -Float.MAX_VALUE.toInt(), max: Int = Float.MAX_VALUE.toInt(), onChange: OnChange<Int> = { }): Boolean {
	val ptr = intArrayOf(value)

	val result = ImGui.sliderInt(label, ptr, min, max, format)
	if (result) onChange(ptr[0])
	return result
}
/**
 * Draws a slider input for [value] and [value1], invoking [onChange] with the updated values if changed.
 * Returns `true` when changed.
 */
inline fun sliderInput(label: String, value: Int, value1: Int, format: String = "%d", min: Int = -Float.MAX_VALUE.toInt(), max: Int = Float.MAX_VALUE.toInt(), onChange: OnChange2<Int> = { _, _ -> }): Boolean {
	val ptr = intArrayOf(value, value1)

	val result = ImGui.sliderInt(label, ptr, min, max, format)
	if (result) onChange(ptr[0], ptr[1])
	return result
}
/**
 * Draws a slider input for [value], [value1], and [value2], invoking [onChange] with the updated values if changed.
 * Returns `true` when changed.
 */
inline fun sliderInput(label: String, value: Int, value1: Int, value2: Int, format: String = "%d", min: Int = -Float.MAX_VALUE.toInt(), max: Int = Float.MAX_VALUE.toInt(), onChange: OnChange3<Int> = { _, _, _ -> }): Boolean {
	val ptr = intArrayOf(value, value1, value2)

	val result = ImGui.sliderInt(label, ptr, min, max, format)
	if (result) onChange(ptr[0], ptr[1], ptr[2])
	return result
}

/**
 * Current window storage access.
 */
object Storage {
	/**
	 * Returns a hash for [id] within the current ID stack for use in [get] and [set].
	 */
	fun key(id: String): Int = ImGui.getID(id)

	/**
	 * Returns the current value for [key], or [defaultValue] if it is not set.
	 */
	operator fun get(key: Int, defaultValue: Byte): Byte = ImGui.getStateStorage().getInt(key, defaultValue.toInt()).toByte()
	/**
	 * Returns the current value for [key], or [defaultValue] if it is not set.
	 */
	operator fun get(key: Int, defaultValue: Short): Short = ImGui.getStateStorage().getInt(key, defaultValue.toInt()).toShort()
	/**
	 * Returns the current value for [key], or [defaultValue] if it is not set.
	 */
	operator fun get(key: Int, defaultValue: Int): Int = ImGui.getStateStorage().getInt(key, defaultValue)
	/**
	 * Returns the current value for [key], or [defaultValue] if it is not set.
	 */
	operator fun get(key: Int, defaultValue: Float): Float = ImGui.getStateStorage().getFloat(key, defaultValue)
	/**
	 * Returns the current value for [key], or [defaultValue] if it is not set.
	 */
	operator fun get(key: Int, defaultValue: Double): Double = ImGui.getStateStorage().getFloat(key, defaultValue.toFloat()).toDouble()
	/**
	 * Returns the current value for [key], or [defaultValue] if it is not set.
	 */
	operator fun get(key: Int, defaultValue: Boolean): Boolean = ImGui.getStateStorage().getBool(key, defaultValue)

	/**
	 * Assigns [key] to [value].
	 */
	operator fun set(key: Int, value: Byte) {
		ImGui.getStateStorage().setInt(key, value.toInt())
	}
	/**
	 * Assigns [key] to [value].
	 */
	operator fun set(key: Int, value: Short) {
		ImGui.getStateStorage().setInt(key, value.toInt())
	}
	/**
	 * Assigns [key] to [value].
	 */
	operator fun set(key: Int, value: Int) {
		ImGui.getStateStorage().setInt(key, value)
	}
	/**
	 * Assigns [key] to [value].
	 */
	operator fun set(key: Int, value: Float) {
		ImGui.getStateStorage().setFloat(key, value)
	}
	/**
	 * Assigns [key] to [value].
	 */
	operator fun set(key: Int, value: Double) {
		ImGui.getStateStorage().setFloat(key, value.toFloat())
	}
	/**
	 * Assigns [key] to [value].
	 */
	operator fun set(key: Int, value: Boolean) {
		ImGui.getStateStorage().setBool(key, value)
	}
}

/**
 * Mouse-specific configuration and actions.
 * Supported buttons defined by [ImGuiMouseButton] and the backend - like `glfw`.
 */
object Mouse {
	var cursor: Int
		get() = ImGui.getMouseCursor()
		set(value) = ImGui.setMouseCursor(value)

	val x: Int
		get() = ImGui.getMousePosX().toInt()
	val y: Int
		get() = ImGui.getMousePosY().toInt()

	/**
	 * Runs [op] and returns `true` when mouse [button] is clicked.
	 */
	inline fun onClick(button: Int = ImGuiMouseButton.Left, op: Op = {}): Boolean {
		val result = ImGui.isMouseClicked(button)
		if (result) op()
		return result
	}
	/**
	 * Runs [op] and returns `true` when mouse [button] is double-clicked.
	 */
	inline fun onDoubleClick(button: Int = ImGuiMouseButton.Left, op: Op = {}): Boolean {
		val result = ImGui.isMouseDoubleClicked(button)
		if (result) op()
		return result
	}

	/**
	 * Runs [op] and returns `true` when mouse [button] is held down.
	 */
	inline fun onDown(button: Int = ImGuiMouseButton.Left, op: Op = {}): Boolean {
		val result = ImGui.isMouseDown(button)
		if (result) op()
		return result
	}
	/**
	 * Runs [op] and returns `true` when mouse [button] is released.
	 */
	inline fun onRelease(button: Int = ImGuiMouseButton.Left, op: Op = {}): Boolean {
		val result = ImGui.isMouseReleased(button)
		if (result) op()
		return result
	}

	/**
	 * Invokes [onChange] with the number of scrolled units when mouse wheel is scrolled vertically.
	 */
	inline fun onScroll(onChange: OnChange<Float> = { _ -> }): Boolean {
		val value = ImGui.getIO().mouseWheel
		val result = value != 0f
		if (result) onChange(value)
		return result
	}
}
/**
 * Key(board)-specific configuration and actions.
 * Supported keys defined by the backend - like `glfw`.
 */
object Key {
	/**
	 * Runs [op] and returns `true` when [key] is pressed.
	 */
	inline fun onPress(key: Int, op: Op = {}): Boolean {
		val result = ImGui.isKeyPressed(key, false)
		if (result) op()
		return result
	}
	/**
	 * Runs [op] and returns `true` when [key] is held down.
	 */
	inline fun onDown(key: Int, op: Op = {}): Boolean {
		val result = ImGui.isKeyDown(key)
		if (result) op()
		return result
	}
	/**
	 * Runs [op] and returns `true` when [key] is released.
	 */
	inline fun onRelease(key: Int, op: Op = {}): Boolean {
		val result = ImGui.isKeyReleased(key)
		if (result) op()
		return result
	}
}

/**
 * Custom draw list operations.
 */
object Draw {
	/**
	 * Current screen cursor coordinates.
	 */
	val cursor: Cursor = Cursor()

	/**
	 * Invokes [op] using [viewport]'s foreground draw list.
	 */
	inline fun fg(viewport: ImGuiViewport, op: ImDrawList.() -> Unit) {
		ImGui.getForegroundDrawList(viewport).op()
	}
	/**
	 * Invokes [op] using [viewport]'s background draw list.
	 */
	inline fun bg(viewport: ImGuiViewport, op: ImDrawList.() -> Unit) {
		ImGui.getBackgroundDrawList(viewport).op()
	}

	/**
	 * Invokes [op] using the current window's draw list.
	 */
	inline fun window(op: ImDrawList.() -> Unit) {
		ImGui.getWindowDrawList().op()
	}

	class Cursor internal constructor() {
		val x: Float
			get() = ImGui.getCursorScreenPosX()
		val y: Float
			get() = ImGui.getCursorScreenPosY()
	}
}

/**
 * Quick access to viewports.
 */
object Viewport {
	val main: ImGuiViewport
		get() = ImGui.getMainViewport()
	val window: ImGuiViewport
		get() = ImGui.getWindowViewport()
}

/**
 * Layout options and helpers.
 */
object Layout {
	/**
	 * Remaining available content space starting from the current cursor.
	 */
	val free: Free = Free()
	/**
	 * Current window cursor coordinates.
	 */
	val cursor: Cursor = Cursor()

	/**
	 * Width to fill available content space - with a safe minimum value.
	 */
	val stretchWidth: Float
		get() = max(Style.font.size * 16f, free.x)

	/**
	 * Returns the total height (including spacing) of [n] lines of text.
	 */
	fun lineHeight(n: Int): Float = ImGui.getTextLineHeightWithSpacing() * n
	/**
	 * Returns the total height (including spacing) of [n] lines of text.
	 */
	fun lineHeight(n: Double): Float = ImGui.getTextLineHeightWithSpacing() * n.toFloat()

	/**
	 * Returns the width of [text].
	 * Ignores hidden areas of labels (i.e ##).
	 */
	fun textWidth(text: String): Float {
		val ptr = tVec2
		ImGui.calcTextSize(ptr, text)
		return ptr.x
	}

	/**
	 * Runs [op] within a context setting [width] item width and returns its result.
	 */
	inline fun <T> width(width: Float, op: () -> T): T {
		ImGui.pushItemWidth(width)
		val result = op()
		ImGui.popItemWidth()

		return result
	}

	class Free internal constructor() {
		val x: Float
			get() = ImGui.getContentRegionAvailX()
		val y: Float
			get() = ImGui.getContentRegionAvailY()
	}

	class Cursor internal constructor() {
		var x: Float
			get() = ImGui.getCursorPosX()
			set(value) = ImGui.setCursorPosX((value))
		var y: Float
			get() = ImGui.getCursorPosY()
			set(value) = ImGui.setCursorPosY(value)
	}
}
/**
 * Current style configuration.
 */
object Style {
	private val style: ImGuiStyle
		get() = ImGui.getStyle()

	/**
	 * Spacing style configuration.
	 */
	val spacing: Spacing = Spacing()
	/**
	 * Font configuration.
	 */
	val font: Font = Font()

	/**
	 * Returns the color set for styling [idx] elements.
	 */
	fun color(idx: Int = ImGuiCol.Text): Int = ImGui.getColorU32(idx)

	class Spacing internal constructor() {
		val x: Float
			get() = style.itemSpacingX
		val y: Float
			get() = style.itemSpacingY
	}

	class Font internal constructor() {
		val current: ImFont
			get() = ImGui.getFont()
		val size: Int
			get() = ImGui.getFontSize()
	}
}

object Ctx {
	object Drag {
		/**
		 * Sets the dragged [payload], with optional unique [id] and [condition].
		 */
		fun setDragDropPayload(payload: Any, id: String? = null, condition: Int = ImGuiCond.None) {
			id?.let { ImGui.setDragDropPayload(it, payload, condition) } ?: ImGui.setDragDropPayload(payload, condition)
		}
	}

	object Drop {
		/**
		 * If the current [Drag.setDragDropPayload] payload is a [T] (and optionally also matches unique [id]), invokes [op] with it.
		 */
		inline fun <reified T> useDragDropPayload(id: String? = null, flags: Int = ImGuiCond.None, op: (T) -> Unit) {
			val payload = id?.let { ImGui.acceptDragDropPayload(it, flags) } ?: ImGui.acceptDragDropPayload(T::class.java, flags)
			payload?.let(op)
		}
	}

	object DockSpace {
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
	}

	object Table {
		/**
		 * Configures column with header `label` and `flags`.
		 * Subsequent calls to this configure the subsequent column.
		 * Submit config with [headersRow].
		 */
		fun configColumn(label: String, flags: Int = ImGuiTableColumnFlags.None) {
			ImGui.tableSetupColumn(label, flags)
		}
		/**
		 * Configures a block of `numCol x numRow` from the top-left edge to stay visible when scrolled.
		 */
		fun scrollFreeze(numCol: Int, numRow: Int) {
			ImGui.tableSetupScrollFreeze(numCol, numRow)
		}
		/**
		 * Writes a table headers row according to prior [configColumn]s.
		 */
		fun headersRow() {
			ImGui.tableHeadersRow()
		}

		/**
		 * Runs [op] in a new table column.
		 */
		inline fun column(op: Op) {
			if (ImGui.tableNextColumn()) op()
		}
	}

	object Menu {
		/**
		 * Draws a menu item of [label], invoking [onClick] when it is selected.
		 * Returns `true` when selected.
		 */
		inline fun menuItem(label: String, onClick: Op): Boolean {
			val result = ImGui.menuItem(label)
			if (result) onClick()
			return result
		}
	}

	open class PlotModifier {
		val x1: Axis = Axis(ImPlotAxis.X1)
		val x2: Axis = Axis(ImPlotAxis.X2)
		val x3: Axis = Axis(ImPlotAxis.X3)
		val y1: Axis = Axis(ImPlotAxis.Y1)
		val y2: Axis = Axis(ImPlotAxis.Y2)
		val y3: Axis = Axis(ImPlotAxis.Y3)

		/**
		 * Runs [op] against the 1st X-axis.
		 */
		inline fun axisX1(op: Axis.() -> Unit) {
			x1.op()
		}
		/**
		 * Runs [op] against the 2nd X-axis.
		 */
		inline fun axisX2(op: Axis.() -> Unit) {
			x2.op()
		}
		/**
		 * Runs [op] against the 3rd X-axis.
		 */
		inline fun axisX3(op: Axis.() -> Unit) {
			x3.op()
		}
		/**
		 * Runs [op] against the 1st Y-axis.
		 */
		inline fun axisY1(op: Axis.() -> Unit) {
			y1.op()
		}
		/**
		 * Runs [op] against the 2nd Y-axis.
		 */
		inline fun axisY2(op: Axis.() -> Unit) {
			y2.op()
		}
		/**
		 * Runs [op] against the 3rd Y-axis.
		 */
		inline fun axisY3(op: Axis.() -> Unit) {
			y3.op()
		}

		/**
		 * Runs [op] in a tooltip when [label] legend entry is hovered.
		 */
		inline fun legendTooltip(label: String, op: Op) {
			if (ImPlot.isLegendEntryHovered(label) && !ImGui.isAnyMouseDown()) {
				ImGui.beginTooltip()
				op()
				ImGui.endTooltip()
			}
		}
		/**
		 * Runs [op] in a popup over [label] legend entry.
		 */
		inline fun legendPopup(label: String, button: Int = ImGuiMouseButton.Right, op: Op) {
			if (ImPlot.beginLegendPopup(label, button)) {
				op()
				ImPlot.endLegendPopup()
			}
		}

		/**
		 * Runs [op] when the plot area is dragged while holding `CTRL`.
		 */
		inline fun onDragPlot(flags: Int = ImGuiDragDropFlags.None, op: Drag.() -> Unit) {
			if (ImPlot.beginDragDropSourcePlot(flags)) {
				Drag.op()
				ImPlot.endDragDropSource()
			}
		}
		/**
		 * Runs [op] when [axis] is dragged while holding `CTRL`.
		 */
		inline fun onDragAxis(axis: Int = ImPlotAxis.X1, flags: Int = ImGuiDragDropFlags.None, op: Drag.() -> Unit) {
			if (ImPlot.beginDragDropSourceAxis(axis, flags)) {
				Drag.op()
				ImPlot.endDragDropSource()
			}
		}
		/**
		 * Runs [op] when [label] legend entry is dragged.
		 */
		inline fun onDragLegend(label: String, flags: Int = ImGuiDragDropFlags.None, op: Drag.() -> Unit) {
			if (ImPlot.beginDragDropSourceItem(label, flags)) {
				Drag.op()
				ImPlot.endDragDropSource()
			}
		}

		/**
		 * Runs [op] when a dragged payload is dropped on the plot area.
		 */
		inline fun onDropPlot(op: Drop.() -> Unit) {
			if (ImPlot.beginDragDropTargetPlot()) {
				Drop.op()
				ImPlot.endDragDropTarget()
			}
		}
		/**
		 * Runs [op] when a dragged payload is dropped on [axis].
		 */
		inline fun onDropAxis(axis: Int = ImPlotAxis.X1, op: Drop.() -> Unit) {
			if (ImPlot.beginDragDropTargetAxis(axis)) {
				Drop.op()
				ImPlot.endDragDropTarget()
			}
		}
		/**
		 * Runs [op] when a dragged payload is dropped on the legend.
		 */
		inline fun onDropLegend(op: Drop.() -> Unit) {
			if (ImPlot.beginDragDropTargetLegend()) {
				Drop.op()
				ImPlot.endDragDropTarget()
			}
		}

		/**
		 * Adds a legend entry [label] without values.
		 */
		fun dummy(label: String) {
			ImPlot.plotDummy(label)
		}
	}

	object Plot : PlotModifier() {
		/**
		 * Plots line data for [label] consisting of [xs] to [ys], starting at [offset].
		 */
		fun line(label: String, xs: DoubleArray, ys: DoubleArray, offset: Int = 0) {
			ImPlot.plotLine(label, xs, ys, xs.size, offset)
		}

		/**
		 * Plots scatter data for [label] consisting of [xs] to [ys], starting at [offset].
		 */
		fun scatter(label: String, xs: IntArray, ys: IntArray, offset: Int = 0) {
			ImPlot.plotScatter(label, xs, ys, xs.size, offset)
		}
		/**
		 * Plots scatter data for [label] consisting of [xs] to [ys], starting at [offset].
		 */
		fun scatter(label: String, xs: DoubleArray, ys: DoubleArray, offset: Int = 0) {
			ImPlot.plotScatter(label, xs, ys, xs.size, offset)
		}

		/**
		 * Plots [text] at ([x], [y]) point.
		 * Can optionally render [vertical].
		 */
		fun text(text: Any, x: Double, y: Double, vertical: Boolean = false) {
			ImPlot.plotText(text.toString(), x, y, vertical)
		}

		/**
		 * Renders a draggable [id]'d point at ([x], [y]).
		 * Returns `true` when dragged.
		 */
		inline fun dragPoint(id: Int, x: Double, y: Double, color: Int = ImColor.rgba(255, 255, 255, 255), size: Float = 4f, flags: Int = ImPlotDragToolFlags.None, onChange: OnChange2<Double> = { _, _ -> }): Boolean {
			val xPtr = pointer(x)
			val yPtr = pointer1(y)
			val colorPtr = pointer((color and 255) / 255f, (color ushr 8 and 255) / 255f, (color ushr 16 and 255) / 255f, (color ushr 24 and 255) / 255f)

			val result = ImPlot.dragPoint(id, xPtr, yPtr, colorPtr, size, flags)
			if (result) onChange(xPtr.get(), yPtr.get())
			return result
		}
	}

	class Axis internal constructor(private val id: Int) {
		private val horizontal = id >= ImPlotAxis.X1 && id <= ImPlotAxis.X3

		/**
		 * Configures this axis with [label] and [flags].
		 * Calling this without any arguments will simply enable the axis with default setup.
		 */
		fun setup(label: String? = null, flags: Int = ImPlotAxisFlags.None) {
			ImPlot.setupAxis(id, label, flags)
		}
		/**
		 * Sets [min] and [max] limits with [condition].
		 */
		fun limit(min: Double, max: Double, condition: Int = ImPlotCond.None) {
			ImPlot.setupAxisLimits(id, min, max, condition)
		}
		/**
		 * Sets the [format] of axis labels.
		 */
		fun format(format: String) {
			ImPlot.setupAxisFormat(id, format)
		}
		/**
		 * Sets tick [labels] from [min] to [max].
		 */
		fun ticks(min: Double, max: Double, labels: Array<String>) {
			ImPlot.setupAxisTicks(id, min, max, labels.size, labels, false)
		}

		/**
		 * Renders a draggable [id]'d guide line at [value].
		 * Returns `true` when dragged.
		 */
		fun dragLine(id: Int, value: Double, color: Int = ImColor.rgba(255, 255, 255, 255), size: Float = 1f, flags: Int = ImPlotDragToolFlags.None, onChange: OnChange<Double> = {}): Boolean {
			val ptr = tDouble.apply { set(value) }
			val colorPtr = tVec4.apply { set((color and 255) / 255f, (color ushr 8 and 255) / 255f, (color ushr 16 and 255) / 255f, (color ushr 24 and 255) / 255f) }

			val result = if (horizontal) ImPlot.dragLineX(id, ptr, colorPtr, size, flags) else ImPlot.dragLineY(id, ptr, colorPtr, size, flags)
			if (result) onChange(ptr.get())
			return result
		}
	}
}

/**
 * Arbitrary operation.
 */
typealias Op = () -> Unit
/**
 * Invoked with a changed `T` value.
 */
typealias OnChange<T> = (T) -> Unit
/**
 * Invoked with 2 changed `T` values.
 */
typealias OnChange2<T> = (T, T) -> Unit
/**
 * Invoked with 3 changed `T` values.
 */
typealias OnChange3<T> = (T, T, T) -> Unit
