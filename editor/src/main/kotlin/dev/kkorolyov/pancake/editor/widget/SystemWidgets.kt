package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.column
import dev.kkorolyov.pancake.editor.indented
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.ptr
import dev.kkorolyov.pancake.editor.table
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.utility.PerfMonitor
import dev.kkorolyov.pancake.platform.utility.Sampler
import imgui.ImGui
import imgui.flag.ImGuiSelectableFlags
import imgui.flag.ImGuiTableFlags
import kotlin.math.roundToInt

/**
 * Renders overall system information from [perfMonitor].
 */
class SystemsTable(private val perfMonitor: PerfMonitor) : Widget {
	private val selection = SystemDetails()
	private val details = Window("", selection, false.ptr())
	private val showHooksPtr = false.ptr()

	override fun invoke() {
		ImGui.checkbox("show hooks", showHooksPtr)

		table("systems", 4, ImGuiTableFlags.Reorderable or ImGuiTableFlags.Resizable) {
			ImGui.tableSetupColumn("System")
			ImGui.tableSetupColumn("Signature")
			ImGui.tableSetupColumn("Tick time (ns)")
			ImGui.tableSetupColumn("TPS")
			ImGui.tableSetupScrollFreeze(1, 1)
			ImGui.tableHeadersRow()

			perfMonitor.systems.forEach { (system, sampler) ->
				val name = system::class.simpleName

				if (name != null || showHooksPtr.get()) {
					column {
						// hook systems (abstract classes most likely) have no details to show
						name?.let {
							if (ImGui.selectable(it, false, ImGuiSelectableFlags.SpanAllColumns)) {
								selection.value = system to sampler
								details.visible = true
								details.label = "${system::class.simpleName}###systemDetails"
							}
						} ?: ImGui.text("hook")
					}
					column {
						ImGui.text(system.signature.asSequence().map { it.simpleName }.joinToString())
					}
					column {
						ImGui.text(sampler.value.toString())
					}
					column {
						ImGui.text((1e9 / sampler.value).roundToInt().toString())
					}
				}
			}
		}

		details()
	}
}

/**
 * Renders detailed system information for the current [value].
 */
class SystemDetails(
	/**
	 * Value to render.
	 */
	var value: Pair<GameSystem, Sampler>? = null
) : Widget {
	override fun invoke() {
		value?.let { (system, sampler) ->
			ImGui.text("Tick time (ns)")
			indented {
				ImGui.text(sampler.value.toString())
			}

			ImGui.text("TPS")
			indented {
				ImGui.text((1e9 / sampler.value).roundToInt().toString())
			}

			ImGui.text("Signature")
			indented {
				list("##signature") {
					system.signature.forEach {
						ImGui.selectable(it.simpleName.toString())
					}
				}
			}
		}
	}
}
