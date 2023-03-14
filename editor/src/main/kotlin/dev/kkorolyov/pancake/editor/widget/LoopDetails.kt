package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.field
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.platform.GameEngine
import dev.kkorolyov.pancake.platform.Pipeline
import imgui.ImGui
import imgui.type.ImBoolean
import imgui.type.ImDouble
import kotlin.math.roundToInt

private val suspendLock = object {}

/**
 * Renders information about [engine]'s loop state.
 */
class LoopDetails(private val engine: GameEngine) : Widget {
	private val activePtr = ImBoolean()
	private val speedPtr = ImDouble()

	override fun invoke() {
		activeToggle()
		ImGui.sameLine()
		speed()

		summary()
	}

	private fun activeToggle() {
		activePtr.set(!engine.suspend.isActive)
		if (ImGui.checkbox("active", activePtr)) {
			if (activePtr.get()) engine.suspend.remove(suspendLock) else engine.suspend.add(suspendLock)
		}
	}

	private fun speed() {
		speedPtr.set(engine.speed)
		if (ImGui.inputDouble("##speed", speedPtr, 0.1, 1.0, "%.2f")) engine.speed = speedPtr.get()
		tooltip("Current speed scale")
	}

	private fun summary() {
		field("Tick time (ns)", engine.sampler.value)
		field("TPS", (1e9 / engine.sampler.value).roundToInt())
		field("Pipelines", engine.size())
		field("Systems", engine.sumOf(Pipeline::size))
	}
}
