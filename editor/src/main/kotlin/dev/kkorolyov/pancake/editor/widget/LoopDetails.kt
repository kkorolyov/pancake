package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.History
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.calcWidth
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.editor.lineHeight
import dev.kkorolyov.pancake.editor.table
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.platform.GameEngine
import dev.kkorolyov.pancake.platform.Pipeline
import imgui.ImGui
import imgui.flag.ImGuiTableFlags

private val suspendLock = object {}

/**
 * Renders information about [engine]'s loop state.
 */
class LoopDetails(private val engine: GameEngine) : Widget {
	private val summaryTemplate = "TPS: %.2f [%.2f, %.2f]"
	private val history = History(10, 1000)
	private val historyWidth by lazy { calcWidth(summaryTemplate.format(100f, 100f, 100f)) }
	private val summary = history.summary("##main")

	override fun invoke() {
		tps()

		activeToggle()
		ImGui.sameLine()
		speed()

		summary()
	}

	private fun tps() {
		history("##main", historyWidth, lineHeight(2)) {
			line("##main", 1e9 / engine.sampler.value)
		}
		text(summaryTemplate.format(summary.avg, summary.min, summary.max))
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
