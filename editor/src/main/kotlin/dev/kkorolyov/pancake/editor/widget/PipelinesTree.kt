package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.indented
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.editor.tree
import dev.kkorolyov.pancake.platform.Pipeline
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

