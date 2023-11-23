package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.column
import dev.kkorolyov.pancake.editor.table
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.editor.tree
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.Pipeline
import dev.kkorolyov.pancake.platform.entity.Entity
import imgui.flag.ImGuiTableFlags
import kotlin.math.roundToInt

/**
 * Renders overall information for [pipelines].
 * If [dragDropId] is provided, emits drag-drop payloads to it containing the selected [GameSystem].
 */
class PipelinesTree(private val pipelines: Collection<Pipeline>, private val dragDropId: String? = null) : Widget {
	private val details = mutableMapOf<Pipeline, SystemsTable>()

	override fun invoke() {
		pipelines.forEachIndexed { i, pipeline ->
			tree("Pipeline $i") {
				table("pipeline.${i}", 2, flags = ImGuiTableFlags.SizingFixedSame) {
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

