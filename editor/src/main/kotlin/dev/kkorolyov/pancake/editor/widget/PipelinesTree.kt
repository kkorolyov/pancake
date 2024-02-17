package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.History
import dev.kkorolyov.pancake.editor.Layout
import dev.kkorolyov.pancake.editor.MagFormat
import dev.kkorolyov.pancake.editor.Style
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.table
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.editor.tree
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.Pipeline
import imgui.flag.ImGuiTableFlags
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Renders overall information for [pipelines].
 * If [dragDropId] is provided, emits drag-drop payloads to it containing the selected [GameSystem].
 */
class PipelinesTree(private val pipelines: Iterable<Pipeline>, private val dragDropId: String? = null) : Widget {
	private val details = mutableMapOf<Pipeline, SystemsTable>()
	private val history = History(10, 1000)
	private val historyWidth by lazy { -Style.spacing.x }

	override fun invoke() {
		val historyHeight = max(Layout.lineHeight(4), Layout.free.y / 5)

		pipelines.forEachIndexed { pipelineI, pipeline ->
			var slowestSystem = "none"
			var slowestSystemTPS = 0L
			history("Pipeline $pipelineI", historyWidth, historyHeight) {
				pipeline.forEachIndexed { systemI, system ->
					val id = system::class.simpleName ?: "hook$systemI"
					val tps = system.sampler.value

					if (tps > slowestSystemTPS) {
						slowestSystemTPS = tps
						slowestSystem = id
					}

					line("$id##$pipelineI", tps.toDouble())
				}

				legendTooltip("*slowest") {
					text("%s (%s)".format(slowestSystem, MagFormat.seconds(slowestSystemTPS)))
				}
			}
		}

		// TODO rejigger
		pipelines.forEachIndexed { pipelineI, pipeline ->
			tree("Pipeline $pipelineI") {
				table("pipeline.${pipelineI}", 2, flags = ImGuiTableFlags.SizingFixedSame) {
					column { text("Tick time (ns)") }
					column { text(pipeline.sampler.value) }

					column { text("TPS") }
					column { text((1e9 / pipeline.sampler.value).roundToInt()) }

					column { text("Slowest system") }
					column { text(pipeline.maxBy { it.sampler.value }::class.simpleName ?: "some hook") }
				}

				details.getOrPut(pipeline) { SystemsTable(pipeline.toList(), dragDropId) }()
			}
		}
	}
}

