package dev.kkorolyov.pancake.editor

import dev.kkorolyov.pancake.editor.widget.Window
import dev.kkorolyov.pancake.graphics.resource.Texture
import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.math.Vector3
import imgui.ImVec2
import imgui.extension.implot.ImPlot
import imgui.extension.implot.flag.ImPlotAxisFlags
import imgui.extension.implot.flag.ImPlotFlags
import imgui.flag.ImGuiComboFlags
import imgui.flag.ImGuiCond
import imgui.flag.ImGuiDir
import imgui.flag.ImGuiDragDropFlags
import imgui.flag.ImGuiHoveredFlags
import imgui.flag.ImGuiInputTextFlags
import imgui.flag.ImGuiMouseButton
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
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

// reuse the same carrier to all imgui input functions
// public only to allow inline functions
/** NO TOUCHY */
val tString by ThreadLocal.withInitial(::ImString)
/** NO TOUCHY */
val tBoolean by ThreadLocal.withInitial(::ImBoolean)
/** NO TOUCHY */
val tInt by ThreadLocal.withInitial(::ImInt)
/** NO TOUCHY */
val tFloat2 by ThreadLocal.withInitial { FloatArray(2) }
/** NO TOUCHY */
val tFloat3 by ThreadLocal.withInitial { FloatArray(3) }
/** NO TOUCHY */
val tDouble by ThreadLocal.withInitial(::ImDouble)
/** NO TOUCHY */
val tVector2 by ThreadLocal.withInitial(Vector2::of)
/** NO TOUCHY */
val tVector3 by ThreadLocal.withInitial(Vector3::of)
/** NO TOUCHY */
val tVec2 by ThreadLocal.withInitial(::ImVec2)
/** NO TOUCHY */
val ctxTable = CtxTable()
/** NO TOUCHY */
val ctxMenu = CtxMenu()
/** NO TOUCHY */
val ctxPlot = CtxPlot()

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
	tooltip(ImGuiHoveredFlags.None, value)
}
/**
 * Draws [value] as text in a tooltip when the last set item is hovered.
 */
fun tooltip(flags: Int, value: Any) {
	if (ImGui.isItemHovered(flags)) ImGui.setTooltip(value.toString())
}
/**
 * Runs [op] in a tooltip when the last set item is hovered.
 */
inline fun tooltip(flags: Int = ImGuiHoveredFlags.None, op: Op) {
	if (ImGui.isItemHovered(flags)) {
		ImGui.beginTooltip()
		op()
		ImGui.endTooltip()
	}
}

/**
 * Runs [op] in a context menu popup over the last clicked item.
 * Accepts a manual item [id] to bind to.
 */
inline fun contextMenu(id: String? = null, flags: Int = ImGuiPopupFlags.MouseButtonRight, op: CtxMenu.() -> Unit) {
	if (id?.let { ImGui.beginPopupContextItem(it, flags) } ?: ImGui.beginPopupContextItem(flags)) {
		ctxMenu.op()
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
 * Runs [op] when the last set item is dragged.
 * [op] should include a call to [setDragDropPayload] and any calls to draw in the drag-drop preview tooltip.
 * See also: [onDrop]
 */
inline fun onDrag(flags: Int = ImGuiDragDropFlags.None, op: Op) {
	if (ImGui.beginDragDropSource(flags)) {
		op()
		ImGui.endDragDropSource()
	}
}
/**
 * Runs [op] when a dragged payload is dropped on the last set item.
 * See also: [onDrag]
 */
inline fun onDrop(op: Op) {
	if (ImGui.beginDragDropTarget()) {
		op()
		ImGui.endDragDropTarget()
	}
}
/**
 * Should be invoked from within an [onDrag].
 * Sets the dragged [payload], with optional unique [id] and [condition].
 */
fun setDragDropPayload(payload: Any, id: String? = null, condition: Int = ImGuiCond.None) {
	id?.let { ImGui.setDragDropPayload(it, payload, condition) } ?: ImGui.setDragDropPayload(payload, condition)
}
/**
 * Should be invoked from within an [onDrop].
 * If the current [setDragDropPayload] payload is a [T] (and optionally also matches unique [id]), invokes [op] with it.
 */
inline fun <reified T> useDragDropPayload(id: String? = null, flags: Int = ImGuiCond.None, op: (T) -> Unit) {
	val payload = id?.let { ImGui.acceptDragDropPayload(it, flags) } ?: ImGui.acceptDragDropPayload(T::class.java, flags)
	payload?.let(op)
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
inline fun table(id: String, columns: Int, width: Float = 0f, height: Float = 0f, flags: Int = ImGuiTableFlags.None, op: CtxTable.() -> Unit) {
	if (ImGui.beginTable(id, columns, flags, width, height)) {
		ctxTable.op()
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
inline fun menu(label: String, op: CtxMenu.() -> Unit) {
	if (ImGui.beginMenu(label)) {
		ctxMenu.op()
		ImGui.endMenu()
	}
}

/**
 * Invokes [op] within a plot of [label].
 */
inline fun plot(
	label: String,
	xLabel: String? = null,
	yLabel: String? = null,
	width: Float = 0f,
	height: Float = 0f,
	flags: Int = ImPlotFlags.None,
	xFlags: Int = ImPlotAxisFlags.None,
	yFlags: Int = ImPlotAxisFlags.None,
	xMin: Double? = null,
	xMax: Double? = null,
	xLimitCond: Int = ImGuiCond.None,
	yMin: Double? = null,
	yMax: Double? = null,
	yLimitCond: Int = ImGuiCond.None,
	xFormat: String? = null,
	yFormat: String? = null,
	op: CtxPlot.() -> Unit
) {
	val fullXFlags = xFlags or if (xLabel == null) ImPlotAxisFlags.NoLabel else ImPlotAxisFlags.None
	val fullYFlags = yFlags or if (yLabel == null) ImPlotAxisFlags.NoLabel else ImPlotAxisFlags.None

	val noPaddingFlags = ImPlotAxisFlags.NoLabel or ImPlotAxisFlags.NoTickLabels
	val noXPadding = fullYFlags and noPaddingFlags == noPaddingFlags
	val noYPadding = fullXFlags and noPaddingFlags == noPaddingFlags

	if (noXPadding || noYPadding) {
		// manual idx because java bindings wrong
		ImPlot.pushStyleVar(17, tVec2.apply {
			x = if (noXPadding) 0f else ImPlot.getStyle().plotPadding.x
			y = if (noYPadding) 0f else ImPlot.getStyle().plotPadding.y
		})
	}

	if (xMin != null && xMax != null) ImPlot.setNextPlotLimitsX(xMin, xMax, xLimitCond)
	if (yMin != null && yMax != null) ImPlot.setNextPlotLimitsY(yMin, yMax, yLimitCond)

	xFormat?.let(ImPlot::setNextPlotFormatX)
	yFormat?.let(ImPlot::setNextPlotFormatY)

	if (
		ImPlot.beginPlot(
			label,
			xLabel ?: "",
			yLabel ?: "",
			tVec2.apply {
				x = width
				y = height
			},
			flags,
			fullXFlags,
			fullYFlags
		)
	) {
		ctxPlot.op()
		ImPlot.endPlot()
	}

	if (noXPadding || noYPadding) {
		ImPlot.popStyleVar()
	}
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
	if (flip) ImGui.image(texture.id, width, height, 0f, 1f, 1f, 0f)
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
 * Can also be provided [digitWidth] to auto size unlabeled inputs enough to fit [digitWidth] digits without overflow.
 * Returns `true` when changed.
 */
inline fun input(label: String, value: Int, step: Int = 0, stepFast: Int = 0, digitWidth: Int = 3, flags: Int = ImGuiInputTextFlags.None, onChange: OnChange<Int> = {}): Boolean {
	val ptr = tInt
	ptr.set(value)

	val width = if (label.startsWith("##")) calcWidth("${10.0.pow(digitWidth)}${if (step != 0 || stepFast != 0) "+++++" else ""}") else null

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
	val ptr = tDouble
	ptr.set(value)

	val width = if (label.startsWith("##")) calcWidth("${format.format(10.0.pow(digitWidth))}${if (step != 0.0 || stepFast != 0.0) "+++++" else ""}") else null

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
	val ptr = tFloat2
	ptr[0] = value.x.toFloat()
	ptr[1] = value.y.toFloat()

	val width = if (label.startsWith("##")) calcWidth(format.format(10.0.pow(digitWidth))) * 2 else null
	width?.let(ImGui::pushItemWidth)
	val result = ImGui.inputFloat2(label, ptr, format, flags)
	width?.let { ImGui.popItemWidth() }

	if (result) {
		val returnPtr = tVector2
		returnPtr.x = ptr[0].toDouble()
		returnPtr.y = ptr[1].toDouble()

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
	val ptr = tFloat3
	ptr[0] = value.x.toFloat()
	ptr[1] = value.y.toFloat()
	ptr[2] = value.z.toFloat()

	val width = if (label.startsWith("##")) calcWidth(format.format(10.0.pow(digitWidth))) * 3 else null
	width?.let(ImGui::pushItemWidth)
	val result = ImGui.inputFloat3(label, ptr, format, flags)
	width?.let { ImGui.popItemWidth() }

	if (result) {
		val returnPtr = tVector3
		returnPtr.x = ptr[0].toDouble()
		returnPtr.y = ptr[1].toDouble()
		returnPtr.z = ptr[2].toDouble()

		onChange(returnPtr)
	}
	return result
}

/**
 * Draws a drag input for [value], invoking [onChange] with the updated value if changed.
 * Returns `true` when changed.
 */
inline fun dragInput(label: String, value: Int, format: String = "%d", min: Int = Float.MIN_VALUE.toInt(), max: Int = Float.MAX_VALUE.toInt(), speed: Float = 0.2f, onChange: OnChange<Int> = { }): Boolean {
	val ptr = intArrayOf(value)

	val result = ImGui.dragInt(label, ptr, speed, min.toFloat(), max.toFloat(), format)
	if (result) onChange(max(min, min(max, ptr[0])))
	return result
}
/**
 * Draws a drag input for [value] and [value1], invoking [onChange] with the updated values if changed.
 * Returns `true` when changed.
 */
inline fun dragInput(label: String, value: Int, value1: Int, format: String = "%d", min: Int = Float.MIN_VALUE.toInt(), max: Int = Float.MAX_VALUE.toInt(), speed: Float = 0.2f, onChange: OnChange2<Int> = { _, _ -> }): Boolean {
	val ptr = intArrayOf(value, value1)

	val result = ImGui.dragInt2(label, ptr, speed, min.toFloat(), max.toFloat(), format)
	if (result) onChange(max(min, min(max, ptr[0])), max(min, min(max, ptr[1])))
	return result
}
/**
 * Draws a drag input for [value], [value1], and [value2], invoking [onChange] with the updated values if changed.
 * Returns `true` when changed.
 */
inline fun dragInput(label: String, value: Int, value1: Int, value2: Int, format: String = "%d", min: Int = Float.MIN_VALUE.toInt(), max: Int = Float.MAX_VALUE.toInt(), speed: Float = 0.2f, onChange: OnChange3<Int> = { _, _, _ -> }): Boolean {
	val ptr = intArrayOf(value, value1, value2)

	val result = ImGui.dragInt3(label, ptr, speed, min.toFloat(), max.toFloat(), format)
	if (result) onChange(max(min, min(max, ptr[0])), max(min, min(max, ptr[1])), max(min, min(max, ptr[2])))
	return result
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
 * Runs [op] when mouse [button] is clicked.
 * Returns `true` when double-clicked.
 */
inline fun onClick(button: Int = ImGuiMouseButton.Left, op: Op): Boolean {
	val result = ImGui.isMouseClicked(button)
	if (result) op()
	return result
}
/**
 * Runs [op] when mouse [button] is double-clicked.
 * Returns `true` when double-clicked.
 */
inline fun onDoubleClick(button: Int = ImGuiMouseButton.Left, op: Op): Boolean {
	val result = ImGui.isMouseDoubleClicked(button)
	if (result) op()
	return result
}

/**
 * Returns the width of [text].
 * Ignores hidden areas of labels (i.e ##).
 */
fun calcWidth(text: String): Float {
	val ptr = tVec2
	ImGui.calcTextSize(ptr, text)
	return ptr.x
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

class CtxTable internal constructor() {
	/**
	 * Runs [op] in a new table column.
	 */
	inline fun column(op: Op) {
		if (ImGui.tableNextColumn()) op()
	}
}

class CtxMenu internal constructor() {
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

class CtxPlot internal constructor() {
	fun lines(label: String, xs: DoubleArray, ys: DoubleArray, offset: Int = 0) {
		ImPlot.plotLine(label, xs, ys, xs.size, offset)
	}

	fun text(text: Any, x: Double, y: Double, vertical: Boolean = false) {
		ImPlot.plotText(text.toString(), x, y, vertical)
	}
}
