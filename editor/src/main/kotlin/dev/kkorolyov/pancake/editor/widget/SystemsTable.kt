package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.DebouncedValue
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.factory.getWidget
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.editor.onDrag
import dev.kkorolyov.pancake.editor.selectable
import dev.kkorolyov.pancake.editor.table
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.platform.GameSystem
import imgui.ImGui
import imgui.flag.ImGuiSelectableFlags
import imgui.flag.ImGuiTableFlags
import kotlin.math.roundToInt

/**
 * Renders overall information for [systems].
 * If [dragDropId] is provided, emits drag-drop payloads to it containing the selected [GameSystem].
 */
class SystemsTable(private val systems: Collection<GameSystem>, private val dragDropId: String? = null) : Widget {
	private var showHooks = false

	private val current = DebouncedValue<GameSystem, Widget> { getWidget(GameSystem::class.java, it) }

	private val inlineDetails = Popup("inlineDetails")

	override fun invoke() {
		table("systems", 4, flags = ImGuiTableFlags.Resizable or ImGuiTableFlags.SizingStretchProp) {
			ImGui.tableSetupColumn("System")
			ImGui.tableSetupColumn("Signature")
			ImGui.tableSetupColumn("Tick time (ns)")
			ImGui.tableSetupColumn("TPS")
			ImGui.tableSetupScrollFreeze(1, 1)
			ImGui.tableHeadersRow()

			systems.forEach { system ->
				val name = system::class.simpleName

				if (name != null || showHooks) {
					column {
						// hook systems (abstract classes most likely) have no details to show
						name?.let {
							selectable(it, ImGuiSelectableFlags.SpanAllColumns) {
								inlineDetails.open(current.set(system))
							}
							dragDropId?.let { id ->
								onDrag {
									setDragDropPayload(system, id)
									current.set(system)()
								}
							}
						} ?: text("hook")
					}
					column {
						text(name?.let { system.joinToString { it.simpleName } } ?: "")
					}
					column {
						text(system.sampler.value)
					}
					column {
						text((1e9 / system.sampler.value).roundToInt())
					}
				}
			}

			inlineDetails()
		}
		if (systems.any { it::class.simpleName == null }) input("show hooks", showHooks) { showHooks = it }
	}
}
