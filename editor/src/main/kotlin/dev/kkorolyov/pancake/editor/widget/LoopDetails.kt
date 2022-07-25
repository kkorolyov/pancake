package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.indented
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.platform.GameEngine
import dev.kkorolyov.pancake.platform.Pipeline
import imgui.ImGui
import imgui.type.ImDouble
import kotlin.math.roundToInt

/**
 * Renders information about [engine]'s loop state.
 */
class LoopDetails(private val engine: GameEngine) : Widget {
	private val playIcon = Image("icons/play.png")
	private val pauseIcon = Image("icons/pause.png")

	private val speedPtr = ImDouble()

	override fun invoke() {
		activeToggle()
		ImGui.sameLine()
		speed()

		summary()
	}

	private fun summary() {
		text("Tick time (ns)")
		indented {
			text(engine.sampler.value)
		}

		text("TPS")
		indented {
			text((1e9 / engine.sampler.value).roundToInt())
		}

		text("Pipelines")
		indented {
			text(engine.size())
		}

		text("Systems")
		indented {
			text(engine.sumOf(Pipeline::size))
		}
	}

	private fun activeToggle() {
		if ((if (engine.speed == 0.0) pauseIcon else playIcon).clickable()) {
			engine.speed = if (engine.speed == 0.0) 1.0 else 0.0
		}
		tooltip("Current active state")
	}

	private fun speed() {
		speedPtr.set(engine.speed)
		ImGui.inputDouble("##speed", speedPtr, 0.1, 1.0, "%.2f")
		engine.speed = speedPtr.get()
		tooltip("Current speed scale")
	}
}
