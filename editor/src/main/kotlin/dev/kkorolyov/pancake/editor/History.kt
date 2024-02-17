package dev.kkorolyov.pancake.editor

import imgui.extension.implot.flag.ImPlotAxisFlags
import imgui.extension.implot.flag.ImPlotFlags
import imgui.flag.ImGuiCond
import kotlin.math.max
import kotlin.math.min

/**
 * Plots historical values within the last [_interval] seconds, split into `samples` even samples.
 */
class History(
	private var _interval: Int,
	private var _samples: Int,
) {
	val ctx = CtxHistory()

	private var lastMs = 0L
	private var rateMs = (_interval * 1e3).toLong() / _samples

	private var dataX = DoubleArray(_samples)
	private val data = mutableMapOf<String, DoubleArray>()

	// index of the most recent entry
	private var cursor = dataX.size - 1
	private val nextCursor: Int
		get() = (cursor + 1) % _samples

	/**
	 * Controls whether calls to [invoke] sample new data points.
	 */
	var active: Boolean = true
	/**
	 * Number of seconds that this retains historical data across.
	 */
	var interval: Int
		get() = _interval
		set(value) {
			if (value != _interval) {
				_interval = value
				resize(true)
			}
		}
	/**
	 * Number of samples within the interval, evenly split.
	 */
	var samples: Int
		get() = _samples
		set(value) {
			if (value != samples) {
				_samples = value
				resize()
			}
		}

	/**
	 * Time in seconds of the latest historical value.
	 */
	val current: Double
		get() = dataX[cursor]

	private fun resize(copy: Boolean = false) {
		val oldSize = dataX.size

		rateMs = (_interval * 1e3).toLong() / _samples

		dataX = resize(dataX, copy)
		data.replaceAll { _, line -> resize(line, copy) }
		cursor = if (copy && oldSize < dataX.size) oldSize else dataX.size - 1
	}

	private fun resize(array: DoubleArray, copy: Boolean): DoubleArray {
		val result = DoubleArray(_samples)

		if (copy) {
			// copy newest data to cursor
			val newStart = nextCursor - min(0, result.size - array.size)
			val newCount = array.size - newStart
			System.arraycopy(array, newStart, result, 0, newCount)

			// copy older data
			val oldStart = 0
			val oldCount = result.size - newCount
			System.arraycopy(array, oldStart, result, newCount, oldCount)
		}

		return result
	}

	/**
	 * Invokes [op] within the scope of this history plot under [label].
	 */
	inline operator fun invoke(label: String, width: Float = 0f, height: Float = 0f, op: History.CtxHistory.() -> Unit) {
		tick()

		plot(label, flags = ImPlotFlags.NoMenus, xFlags = ImPlotAxisFlags.NoDecorations or ImPlotAxisFlags.Time, yFlags = ImPlotAxisFlags.NoDecorations, xMin = max(0.0, current - interval), xMax = max(interval.toDouble(), current), xLimitCond = ImGuiCond.Always, width = width, height = height) {
			ctx(this, op)

			dummy("*opts")
			legendPopup("*opts") {
				input("active", active) { active = it }

				text("interval | samples")
				dragInput("##intervalSamples", interval, samples, min = 1, max = 10000) { interval, samples ->
					this@History.interval = interval
					this@History.samples = samples
				}
			}
		}
	}
	/**
	 * Updates internal plotting state to the current instant.
	 * Internal helper for [invoke] and should not be called manually.
	 */
	fun tick() {
		if (active) {
			val nowMs = System.currentTimeMillis()
			if (nowMs - lastMs > rateMs) {
				val now = nowMs / 1e3
				lastMs = nowMs

				cursor = nextCursor
				dataX[cursor] = now
			}
		}
	}

	/**
	 * Returns a dynamic summary view over the current values for [label].
	 */
	fun summary(label: String): Summary = Summary(label)

	/**
	 * Dynamic summarized view over the current values of a label.
	 */
	inner class Summary internal constructor(private val id: String) {
		/**
		 * Current minimum value in the interval.
		 */
		val min: Double
			get() = data[id]?.min() ?: 0.0
		/**
		 * Current maximum value in the interval.
		 */
		val max: Double
			get() = data[id]?.max() ?: 0.0
		/**
		 * Current average value in the interval.
		 */
		val avg: Double
			get() = data[id]?.average() ?: 0.0
	}

	inner class CtxHistory internal constructor() {
		lateinit var ctx: CtxPlot

		/**
		 * Opens a history context with underlying [ctx] and invokes [op].
		 * Internal helper for [History.invoke] and should not be called manually.
		 */
		inline operator fun invoke(ctx: CtxPlot, op: CtxHistory.() -> Unit) {
			this.ctx = ctx
			op()
		}

		/**
		 * Updates [label] with [value] at the current latest entry.
		 * Plots a line graph of its historical values.
		 */
		fun line(label: String, value: Double) {
			val lineData = data.getOrPut(label) { DoubleArray(_samples) }

			if (active) lineData[cursor] = value

			ctx.line(label, dataX, lineData, nextCursor)
		}

		/**
		 * Adds [label] legend entry, and runs [op] in a tooltip when it is hovered.
		 */
		inline fun legendTooltip(label: String, op: Op) {
			ctx.dummy(label)
			ctx.legendTooltip(label, op)
		}
	}
}
