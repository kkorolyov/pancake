package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.DebouncedValue
import dev.kkorolyov.pancake.editor.Layout
import dev.kkorolyov.pancake.editor.MagFormat
import dev.kkorolyov.pancake.editor.RealtimePlot
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.factory.getWidget
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.Pipeline
import kotlin.math.max

/**
 * Renders overall information for [pipelines].
 * If [dragDropId] is provided, emits drag-drop payloads to it containing the selected [GameSystem].
 */
class PipelinesList(private val pipelines: Iterable<Pipeline>, private val dragDropId: String? = null) : Widget {
	private val perfGraph = RealtimePlot(10, 1000)

	private val draggingSystem = DebouncedValue<GameSystem, Widget> { getWidget(GameSystem::class.java, it) }

	override fun invoke() {
		var dragging = false

		val historyHeight = max(Layout.lineHeight(4), (Layout.free.y - Layout.lineHeight(1)) / 5)
		pipelines.forEachIndexed { i, pipeline ->
			var slowestSystem = "none"
			var slowestSystemTime = 0L
			perfGraph("Pipeline $i", Layout.stretchWidth, historyHeight) {
				pipeline.forEach { system ->
					val id = system.debugName
					val tickTime = system.sampler.value

					if (tickTime > slowestSystemTime) {
						slowestSystemTime = tickTime
						slowestSystem = id
					}

					line(id, tickTime.toDouble())

					dragDropId?.let { ddId ->
						onDragLegend(id) {
							setDragDropPayload(system, ddId)
							// set here, but render later
							draggingSystem.set(system)
							dragging = true
						}
					}

					legendTooltip(id) {
						text(system.joinToString { it.simpleName })
					}
				}

				dummy("*slowest")
				legendTooltip("*slowest") {
					text("%s (%s)".format(slowestSystem, MagFormat.seconds(slowestSystemTime)))
				}
			}
		}

		// manually render drag source after everything - to avoid issues with nesting calls inside a plot context
		if (dragging) {
			draggingSystem.get()?.let {
				tooltip(force = true) {
					it()
				}
			}
		}
	}
}
