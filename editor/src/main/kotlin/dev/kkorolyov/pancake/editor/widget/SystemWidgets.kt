package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.column
import dev.kkorolyov.pancake.editor.indented
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.onDoubleClick
import dev.kkorolyov.pancake.editor.selectable
import dev.kkorolyov.pancake.editor.table
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.editor.tree
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.Pipeline
import imgui.ImGui
import imgui.flag.ImGuiSelectableFlags
import imgui.flag.ImGuiTableFlags
import org.lwjgl.glfw.GLFW.*
import kotlin.math.roundToInt

/**
 * Renders overall information for [pipelines].
 */
class PipelinesTree(private val pipelines: Collection<Pipeline>) : Widget {
	private val details = mutableMapOf<Pipeline, SystemsTable>()

	override fun invoke() {
		pipelines.forEachIndexed { i, pipeline ->
			tree("Pipeline $i") {
				text("Tick time (ns)")
				indented {
					text(pipeline.sampler.value)
				}

				text("TPS")
				indented {
					text((1e9 / pipeline.sampler.value).roundToInt())
				}

				text("Slowest system")
				indented {
					text(pipeline.maxBy { it.sampler.value }::class.simpleName ?: "some hook")
				}

				details.getOrPut(pipeline) { SystemsTable(pipeline.toList()) }()
			}
		}
	}
}

/**
 * Renders overall information for [systems].
 */
class SystemsTable(private val systems: Collection<GameSystem>) : Widget {
	private var showHooks = false

	private var detail = Widget { text("select a system to preview") }
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
								detail = SystemDetails(system)

								onDoubleClick(GLFW_MOUSE_BUTTON_1) {
									details[it] = { Window(it, detail) }
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
		detail()

		details()
	}
}

/**
 * Renders detailed information for [system].
 */
class SystemDetails(private val system: GameSystem) : Widget {
	override fun invoke() {
		text("Tick time (ns)")
		indented {
			text(system.sampler.value)
		}

		text("TPS")
		indented {
			text((1e9 / system.sampler.value).roundToInt())
		}

		text("Signature")
		indented {
			list("##signature") {
				system.forEach {
					ImGui.selectable(it.simpleName)
				}
			}
		}
	}
}
