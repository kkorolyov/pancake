package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.column
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.editor.table
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.platform.GameEngine
import dev.kkorolyov.pancake.platform.Pipeline
import imgui.ImGui
import imgui.flag.ImGuiTableFlags
import kotlin.math.roundToInt

private val suspendLock = object {}

/**
 * Renders information about [engine]'s loop state.
 */
class LoopDetails(private val engine: GameEngine) : Widget {

	override fun invoke() {
		activeToggle()
		ImGui.sameLine()
		speed()

		summary()
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
		table("summary", 2, ImGuiTableFlags.SizingFixedFit) {
			column { text("Tick time (ns)") }
			column { text(engine.sampler.value) }

			column { text("TPS") }
			column { text((1e9 / engine.sampler.value).roundToInt()) }

			column { text("Pipelines") }
			column { text(engine.size()) }

			column { text("Systems") }
			column { text(engine.sumOf(Pipeline::size)) }
		}
	}
}
