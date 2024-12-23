package dev.kkorolyov.pancake.editor

import dev.kkorolyov.pancake.platform.utility.ArgVerify
import imgui.flag.ImGuiCol
import imgui.flag.ImGuiMouseButton
import imgui.internal.ImGui
import imgui.type.ImBoolean
import imgui.type.ImInt
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round
import kotlin.math.roundToInt

// draw params
private const val tickWidth = 2.0
private const val scrubberWidth = 16.0

// input params
private const val subDivision = 2
private const val zoomStepScale = 0.1
private const val snapThreshold = 5.0

// shared state
private val tlSequencerState = ThreadLocal.withInitial { SequencerState() }

/**
 * Opens a sequencer context for [id].
 * The sequencer region is clamped between ([min], [max]), with tick marks at [interval] offsets.
 */
fun beginSequencer(id: String, min: Int, max: Int, interval: Int, offset: Int): Boolean {
	val state = tlSequencerState.get()
	if (state.active) throw IllegalStateException("started a new sequencer before closing the current one")
	state.active = true

	ArgVerify.betweenInclusive("interval", min, max, interval)
	ArgVerify.betweenInclusive("offset", min, max, offset)

	ImGui.pushID(id)

	// persistent state per ID
	val minKey = Storage.key("min")
	val maxKey = Storage.key("max")
	var currentMin: Int = Storage[minKey, min]
	var currentMax: Int = Storage[maxKey, max]

	// shared state per frame
	var dragging: Boolean = state.dragging
	var currentOffset = if (dragging) state.offset else offset

	val width = Layout.stretchWidth
	val height = Layout.lineHeight(1)
	val scale = width / (currentMax - currentMin)
	val subInterval = interval / subDivision
	Draw.window {
		// header area
		addRectFilled(Draw.cursor.x, Draw.cursor.y, Draw.cursor.x + width, Draw.cursor.y + height, Style.color(ImGuiCol.TableHeaderBg))
		addText(Draw.cursor.x + (width - Layout.textWidth(currentOffset.toString())) / 2, Draw.cursor.y, Style.color(ImGuiCol.Text), currentOffset.toString())

		// ticks
		for (tick in (ceil(currentMin.toDouble() / interval).toInt() * interval)..currentMax step interval) {
			if (tick != min && tick != max) {
				val x0 = (tick - currentMin) * scale + Draw.cursor.x
				val y0 = Draw.cursor.y + (height / 4)

				addLine(x0, y0, x0, Draw.cursor.y + height, Style.color(ImGuiCol.TextDisabled), tickWidth.toFloat())
			}
		}
		for (subtick in (ceil(currentMin.toDouble() / subInterval).toInt() * subInterval)..currentMax step subInterval) {
			if (subtick % interval != 0 && subtick != min && subtick != max) {
				val x0 = (subtick - currentMin) * scale + Draw.cursor.x
				val y0 = Draw.cursor.y + (height / 2)

				addLine(x0, y0, x0, Draw.cursor.y + height, Style.color(ImGuiCol.TextDisabled), tickWidth.toFloat())
			}
		}
		// anchors
		for (anchor in min..max step (max - min)) {
			val x0 = (anchor - currentMin) * scale + Draw.cursor.x
			val y0 = Draw.cursor.y

			addLine(x0, y0, x0, Draw.cursor.y + height, Style.color(ImGuiCol.Text), tickWidth.toFloat())
		}

		// offset is within current viewport
		if (currentOffset in currentMin..currentMax) {
			val x0 = (currentOffset - currentMin) * scale + Draw.cursor.x
			val y0 = Draw.cursor.y
			val halfWidth = (scrubberWidth / 2).toFloat()

			// scrubber element
			addTriangleFilled(x0 - halfWidth, y0, x0 + halfWidth, y0, x0, y0 + halfWidth, Style.color(ImGuiCol.Text))
			addLine(x0, y0, x0, y0 + Layout.lineHeight(1), Style.color(ImGuiCol.Text), tickWidth.toFloat())
		}
	}
	dummy(width, height, "header")

	// change zoom on hover and scroll
	onHover {
		val zoomStep = zoomStepScale * (max - min)

		if (ImGui.getIO().mouseWheel > 0) {
			val cursorRatio = (Mouse.x - Draw.cursor.x) / width
			val minDelta = cursorRatio * zoomStep
			val maxDelta = (1 - cursorRatio) * zoomStep

			currentMin = (currentMin + minDelta).roundToInt()
			currentMax = (currentMax - maxDelta).roundToInt()
		} else if (ImGui.getIO().mouseWheel < 0) {
			val cursorRatio = (Mouse.x - Draw.cursor.x) / width
			val minDelta = cursorRatio * zoomStep
			val maxDelta = (1 - cursorRatio) * zoomStep

			currentMin = (currentMin - minDelta).roundToInt()
			currentMax = (currentMax + maxDelta).roundToInt()
		}

		// reset zoom on middle click
		Mouse.onClick(ImGuiMouseButton.Middle) {
			currentMin = min
			currentMax = max
		}
	}

	// change offset on hover or dragging
	val offsetPtr = pointer(currentOffset)
	val draggingPtr = pointer(dragging)
	dragValue(offsetPtr, draggingPtr, width)
	currentOffset = min(max, max(min, offsetPtr.get()))
	dragging = draggingPtr.get()

	// update persistent state
	Storage[minKey] = currentMin
	Storage[maxKey] = currentMax
	// setup per frame state
	state.offset = currentOffset
	state.min = currentMin
	state.max = currentMax
	state.interval = interval
	state.dragging = dragging
	state.width = width

	return true
}
/**
 * Closes the current sequencer context.
 */
fun endSequencer() {
	val state = tlSequencerState.get()
	if (!state.active) throw IllegalStateException("not in a sequencer context")
	state.active = false

	ImGui.popID()
}
/**
 * If the current sequencer offset is modified, returns `true` and updates [offset] with the current value.
 */
fun sequencerOffset(offset: ImInt): Boolean {
	val state = tlSequencerState.get()
	if (!state.active) throw IllegalStateException("not in a sequencer context")

	if (state.dragging) offset.set(state.offset)
	return state.dragging
}

/**
 * Opens a sequencer track context for [label].
 */
fun beginTrack(label: String): Boolean {
	val state = tlSequencerState.get()
	if (!state.active) throw IllegalStateException("not in a sequencer context")

	ImGui.pushID(label)

	val width = state.width
	val height = Layout.lineHeight(1)

	Draw.window {
		// track area
		addRectFilled(Draw.cursor.x, Draw.cursor.y, Draw.cursor.x + width, Draw.cursor.y + height, Style.color(ImGuiCol.TableRowBg))
		addText(Draw.cursor.x, Draw.cursor.y, Style.color(ImGuiCol.Text), label)

		// scrubber
		val scale = width / (state.max - state.min)
		val x0 = (state.offset - state.min) * scale + Draw.cursor.x
		val y0 = Draw.cursor.y
		addLine(x0, y0 - Style.spacing.y, x0, y0 + Layout.lineHeight(1), Style.color(ImGuiCol.Text), tickWidth.toFloat())
	}

	val startY = Layout.cursor.y
	dummy(width, Layout.lineHeight(1), "track")
	// allow for overlapping keyframes to accept interactions
	ImGui.setItemAllowOverlap()
	// put cursor back on the track for any subsequent overlapping keyframes
	Layout.cursor.y = startY

	return true;
}
/**
 * Closes the current track context.
 */
fun endTrack() {
	val state = tlSequencerState.get()
	if (!state.active) throw IllegalStateException("not in a sequencer context")

	Mouse.onRelease {
		// ensure no situation where an undrawn phantom keyframe stealing focus
		state.activeKeyframe = null
	}

	// track done - move to next line
	newLine()

	ImGui.popID()
}

/**
 * Draws a keyframe for [id] with [offset].
 * Returns `true` if the offset was modified.
 */
fun keyframe(id: String, offset: ImInt): Boolean {
	val state = tlSequencerState.get()
	if (!state.active) throw IllegalStateException("not in a sequencer context")

	var changed = false

	if (state.min <= offset.get() && offset.get() <= state.max) {
		val idHash = Storage.key(id)
		val active = idHash == state.activeKeyframe
		val currentOffset = offset.get()

		val width = state.width
		val height = Layout.lineHeight(1)
		val scale = width / (state.max - state.min)

		val startX = Layout.cursor.x
		val startY = Layout.cursor.y

		// position keyframe area left edge
		Layout.cursor.x += (currentOffset - state.min) * scale - Style.spacing.x

		// get draw coords
		val x0 = Draw.cursor.x + height / 2
		val y0 = Draw.cursor.y + height / 2

		// interactive area + check state
		// avoid area overflowing width and causing a Layout.free.x feedback loop
		dummy(min(width - (Layout.cursor.x - startX), height), height, id)
		val radius = if (onHover()) height / 2 else height / 4

		// reset cursor to a simpler, blank slate for subsequent keyframes
		Layout.cursor.x = startX
		Layout.cursor.y = startY

		// draw
		Draw.window {
			addNgonFilled(x0, y0, radius, Style.color(ImGuiCol.Text), 4)
		}

		if (active || state.activeKeyframe == null) {
			val offsetPtr = pointer(currentOffset)
			val draggingPtr = pointer(active)
			dragValue(offsetPtr, draggingPtr, width)
			// compare against local currentOffset instead of offset, as it may be the same shared instance as offsetPtr
			if (currentOffset != offsetPtr.get()) {
				changed = true
				offset.set(offsetPtr.get())
			}
			state.activeKeyframe = if (draggingPtr.get()) idHash else null
		}
	}
	return changed
}

private fun dragValue(value: ImInt, dragging: ImBoolean, width: Float) {
	if (onHover() || dragging.get()) {
		val state = tlSequencerState.get()
		if (!state.active) throw IllegalStateException("not in a sequencer context")
		val scale = width / (state.max - state.min)

		Mouse.onDown() {
			var result = (max(0f, min(width, (Mouse.x - Draw.cursor.x))) / scale + state.min).roundToInt()
			val scaledSnapThreshold = snapThreshold / scale
			val subInterval = state.interval / subDivision

			if (result.mod(subInterval) <= scaledSnapThreshold || result.mod(-subInterval) >= -scaledSnapThreshold) {
				result = (round(result.toDouble() / subInterval) * subInterval).toInt()
			}
			value.set(result)
			dragging.set(true)
		}
		Mouse.onRelease {
			dragging.set(false)
		}
	}
}

/**
 * Runs [op] within a sequencer named [id].
 */
inline fun sequencer(id: String, min: Int, max: Int, interval: Int, offset: Int, op: Sequencer.() -> Unit) {
	if (beginSequencer(id, min, max, interval, offset)) {
		Sequencer.op()
		endSequencer()
	}
}

private data class SequencerState(var offset: Int = 0, var min: Int = 0, var max: Int = 0, var interval: Int = 0, var dragging: Boolean = false, var activeKeyframe: Int? = null, var width: Float = 0f, var active: Boolean = false)

object Sequencer {
	/**
	 * Invokes [onChange] with the changed offset of the current sequencer, if it is currently changed.
	 */
	inline fun onChange(onChange: OnChange<Int>): Boolean {
		val ptr = pointer(0)
		val result = sequencerOffset(ptr)
		if (result) onChange(ptr.get())
		return result
	}

	/**
	 * Runs [op] within a track labeled [label].
	 */
	inline fun track(label: String, op: Track.() -> Unit) {
		if (beginTrack(label)) {
			Track.op()
			endTrack()
		}
	}

	object Track {
		/**
		 * Draws a keyframe for [id] with [offset] value.
		 * If offset is modified, invokes [onChange] with the modified value and returns `true`.
		 */
		fun keyframe(id: String, offset: Int, onChange: OnChange<Int> = {}): Boolean {
			val ptr = pointer(offset)
			val result = keyframe(id, ptr)
			if (result) onChange(ptr.get())
			return result
		}
	}
}
