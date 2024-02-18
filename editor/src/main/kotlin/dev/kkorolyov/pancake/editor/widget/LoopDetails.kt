package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.History
import dev.kkorolyov.pancake.editor.Layout
import dev.kkorolyov.pancake.editor.MagFormat
import dev.kkorolyov.pancake.editor.Style
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.editor.table
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.platform.GameEngine
import dev.kkorolyov.pancake.platform.Pipeline
import imgui.ImGui
import imgui.flag.ImGuiTableFlags
import kotlin.math.max

private val suspendLock = object {}

/**
 * Renders information about [engine]'s loop state.
 */
class LoopDetails(private val engine: GameEngine) : Widget {
	private val history = History(10, 1000)
	private val historyWidth by lazy { -Style.spacing.x }
	private val summary = history.summary("##main")

	override fun invoke() {
		graphs()

		activeToggle()
		ImGui.sameLine()
		speed()

		summary()
	}

	private fun graphs() {
		val minY = Layout.lineHeight(4)
		val yFree = Layout.free.y

		history("TPS", historyWidth, max(minY, yFree / 5)) {
			line("##main", 1e9 / engine.sampler.value)

			dummy("*stats")
			legendTooltip("*stats") {
				text("%.2f [%.2f, %.2f]".format(summary.avg, summary.min, summary.max))
			}
		}

		var slowestPipeline = "none"
		var slowestPipelineTPS = 0L
		history("Pipelines", historyWidth, max(minY, yFree / 2)) {
			engine.forEachIndexed { i, pipeline ->
				val id = "Pipeline $i"
				val tps = pipeline.sampler.value

				if (tps > slowestPipelineTPS) {
					slowestPipelineTPS = tps
					slowestPipeline = id
				}

				line(id, tps.toDouble())
			}

			dummy("*slowest")
			legendTooltip("*slowest") {
				text("%s (%s)".format(slowestPipeline, MagFormat.seconds(slowestPipelineTPS)))
			}
		}
	}

	private fun activeToggle() {
		input("active", !engine.suspend.isActive) {
			if (it) engine.suspend.remove(suspendLock) else engine.suspend.add(suspendLock)
		}
	}

	private fun speed() {
		input("##speed", engine.speed) { engine.speed = it }
		tooltip("Current speed scale")
	}

	private fun summary() {
		table("summary", 2, flags = ImGuiTableFlags.SizingFixedFit) {
			column { text("Pipelines") }
			column { text(engine.size()) }

			column { text("Systems") }
			column { text(engine.sumOf(Pipeline::size)) }
		}
	}
}
