package dev.kkorolyov.pancake.editor

import imgui.flag.ImGuiCol
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
private const val snapThreshold = 5.0

/**
 * Opens a sequencer context for [id], between [min] and [max], and with tick marks at [interval] offsets.
 */
fun beginSequencer(id: String, min: Int, max: Int, interval: Int): Boolean {
	val state = SequencerState.open()

	ImGui.pushID(id)

	val width = Layout.stretchWidth
	val height = Layout.lineHeight(1)
	val scale = width / (max - min)
	val subInterval = interval / subDivision

	state.startX = Draw.cursor.x
	state.startY = Draw.cursor.y
	state.width = width

	Draw.window {
		// header area
		addRectFilled(Draw.cursor.x, Draw.cursor.y, Draw.cursor.x + width, Draw.cursor.y + height, Style.color(ImGuiCol.TableHeaderBg))

		// ticks
		for (tick in (ceil(min.toDouble() / interval).toInt() * interval)..max step interval) {
			if (tick != min && tick != max) {
				val x0 = (tick - min) * scale + Draw.cursor.x
				val y0 = Draw.cursor.y + (height / 4)

				addLine(x0, y0, x0, Draw.cursor.y + height, Style.color(ImGuiCol.TextDisabled), tickWidth.toFloat())
			}
		}
		for (subtick in (ceil(min.toDouble() / subInterval).toInt() * subInterval)..max step subInterval) {
			if (subtick % interval != 0 && subtick != min && subtick != max) {
				val x0 = (subtick - min) * scale + Draw.cursor.x
				val y0 = Draw.cursor.y + (height / 2)

				addLine(x0, y0, x0, Draw.cursor.y + height, Style.color(ImGuiCol.TextDisabled), tickWidth.toFloat())
			}
		}
	}
	dummy(width, height, "header")

	// setup per frame state
	state.min = min
	state.max = max
	state.interval = interval

	return true
}
/**
 * Closes the current sequencer context.
 */
fun endSequencer() {
	SequencerState.get().active = false

	ImGui.popID()
}

/**
 * Draws a scrubber for the current open sequencer at [offset].
 * Returns `true` if the offset was modified and sets [offset] to the new value.
 */
fun scrubber(offset: ImInt): Boolean {
	val state = SequencerState.get()

	val startOffset = offset.get()

	var dragging = state.dragging
	var currentOffset = startOffset

	// change offset on hover or dragging
	val offsetPtr = pointer(currentOffset)
	val draggingPtr = pointer(dragging)
	dragValue(offsetPtr, draggingPtr)
	currentOffset = offsetPtr.get()
	dragging = draggingPtr.get()

	// always draw value
	Draw.window {
		addText(state.startX + (state.width - Layout.textWidth(currentOffset.toString())) / 2, state.startY, Style.color(ImGuiCol.Text), currentOffset.toString())
	}

	// draw scrubber element if offset is within current viewport
	if (currentOffset in state.min..state.max) {
		val scale = state.width / (state.max - state.min)
		val x0 = (currentOffset - state.min) * scale + state.startX
		val y0 = state.startY
		val halfWidth = (scrubberWidth / 2).toFloat()

		// scrubber element
		Draw.window {
			addTriangleFilled(x0 - halfWidth, y0, x0 + halfWidth, y0, x0, y0 + halfWidth, Style.color(ImGuiCol.Text))
			addLine(x0, y0, x0, y0 + Layout.lineHeight(1), Style.color(ImGuiCol.Text), tickWidth.toFloat())
		}
	}

	state.offset = currentOffset
	state.dragging = dragging

	val result = currentOffset != startOffset
	if (result) offset.set(currentOffset)
	return result
}

/**
 * Draws a marker for the current open sequencer at [offset].
 */
fun drawMarker(offset: Int) {
	val state = SequencerState.get()

	val scale = state.width / (state.max - state.min)
	val x0 = (offset - state.min) * scale + state.startX
	val y0 = state.startY

	Draw.window {
		addLine(x0, y0, x0, state.startY + Layout.lineHeight(1), Style.color(ImGuiCol.Text), tickWidth.toFloat())
	}
}

/**
 * Returns the location of the mouse cursor's position on the current open sequencer as the nearest whole offset value.
 */
fun getMouseOffset(): Int {
	val state = SequencerState.get()
	val scale = (state.max - state.min) / state.width

	return (max(0f, min(state.width, (Mouse.x - state.startX))) * scale + state.min).roundToInt()
}
/**
 * Returns the location of the mouse cursor's position on the current open sequencer as a `[0, 1]` ratio.
 */
fun getMouseRatio(): Float {
	val state = SequencerState.get()

	return max(0f, min(1f, (Mouse.x - state.startX) / state.width))
}

/**
 * Opens a sequencer track context for [label].
 */
fun beginTrack(label: String): Boolean {
	val state = SequencerState.get()

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
	// allow for overlapping keyframes to accept interactions
	ImGui.setNextItemAllowOverlap()
	dummy(width, Layout.lineHeight(1), "track")
	// put cursor back on the track for any subsequent overlapping keyframes
	Layout.cursor.y = startY

	return true;
}
/**
 * Closes the current track context.
 */
fun endTrack() {
	val state = SequencerState.get()

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
 * Returns `true` if the offset was modified and sets [offset] to the new value.
 */
fun keyframe(id: String, offset: ImInt): Boolean {
	val state = SequencerState.get()

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
			dragValue(offsetPtr, draggingPtr)
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

private fun dragValue(value: ImInt, dragging: ImBoolean) {
	if (onHover() || dragging.get()) {
		Mouse.onDown() {
			val state = SequencerState.get()
			val scale = (state.max - state.min) / state.width
			val scaledSnapThreshold = snapThreshold * scale
			val subInterval = state.interval / subDivision

			var result = getMouseOffset()

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

private data class SequencerState(var offset: Int = 0, var min: Int = 0, var max: Int = 0, var interval: Int = 0, var dragging: Boolean = false, var activeKeyframe: Int? = null, var width: Float = 0f, var startX: Float = 0f, var startY: Float = 0f, var active: Boolean = false) {
	companion object {
		// shared state
		private val tl = ThreadLocal.withInitial { SequencerState() }

		fun open(): SequencerState {
			val state = tl.get()
			if (state.active) throw IllegalStateException("started a new sequencer before closing the current one")
			state.active = true
			return state
		}

		fun get(): SequencerState {
			val state = tl.get()
			if (!state.active) throw IllegalStateException("not in a sequencer context")
			return state
		}
	}
}

/**
 * Runs [op] within a sequencer named [id].
 */
inline fun sequencer(id: String, min: Int, max: Int, interval: Int, op: Sequencer.() -> Unit) {
	if (beginSequencer(id, min, max, interval)) {
		Sequencer.op()
		endSequencer()
	}
}

object Sequencer {
	/**
	 * Draws a scrubber for the current open sequencer at [offset].
	 * If offset is modified, invokes [onChange] with the new value and returns `true`.
	 */
	inline fun scrubber(offset: Int, onChange: OnChange<Int>): Boolean {
		val ptr = pointer(offset)
		val result = scrubber(ptr)
		if (result) onChange(ptr.get())
		return result
	}

	/**
	 * [drawMarker]
	 */
	fun marker(offset: Int) {
		drawMarker(offset)
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

	/**
	 * [getMouseOffset]
	 */
	val mouseOffset: Int
		get() = getMouseOffset()
	/**
	 * [getMouseRatio]
	 */
	val mouseRatio: Float
		get() = getMouseRatio()

	object Track {
		/**
		 * Draws a keyframe for [id] at [offset].
		 * If offset is modified, invokes [onChange] with the new value and returns `true`.
		 */
		fun keyframe(id: String, offset: Int, onChange: OnChange<Int> = {}): Boolean {
			val ptr = pointer(offset)
			val result = keyframe(id, ptr)
			if (result) onChange(ptr.get())
			return result
		}
	}
}
