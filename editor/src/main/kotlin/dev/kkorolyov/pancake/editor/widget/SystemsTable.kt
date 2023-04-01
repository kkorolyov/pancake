package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.MemoizedContent
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.column
import dev.kkorolyov.pancake.editor.factory.getWidget
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.editor.onDoubleClick
import dev.kkorolyov.pancake.editor.selectable
import dev.kkorolyov.pancake.editor.separator
import dev.kkorolyov.pancake.editor.table
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.platform.GameSystem
import imgui.ImGui
import imgui.flag.ImGuiSelectableFlags
import imgui.flag.ImGuiTableFlags
import org.lwjgl.glfw.GLFW
import kotlin.math.roundToInt

/**
 * Renders overall information for [systems].
 */
class SystemsTable(private val systems: Collection<GameSystem>) : Widget {
	private var showHooks = false

	private val preview = MemoizedContent<GameSystem>({ getWidget(GameSystem::class.java, it) }, Widget { text("Select a system to preview") })
	private val details = WindowManifest<String>()

	override fun invoke() {
		table("systems", 4, ImGuiTableFlags.Resizable or ImGuiTableFlags.SizingStretchProp) {
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
							selectable(it, ImGuiSelectableFlags.SpanAllColumns or ImGuiSelectableFlags.AllowDoubleClick) {
								preview(system)

								onDoubleClick(GLFW.GLFW_MOUSE_BUTTON_1) {
									details[it] = { Window(it, preview.value) }
									preview.reset()
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
		}
		if (systems.any { it::class.simpleName == null }) input("show hooks", showHooks) { showHooks = it }

		separator()
		preview.value()

		details()
	}
}
