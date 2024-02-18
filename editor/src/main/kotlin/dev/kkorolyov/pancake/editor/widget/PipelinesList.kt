package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.DebouncedValue
import dev.kkorolyov.pancake.editor.History
import dev.kkorolyov.pancake.editor.Layout
import dev.kkorolyov.pancake.editor.MagFormat
import dev.kkorolyov.pancake.editor.Style
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.factory.getWidget
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.Pipeline
import kotlin.math.max

/**
 * Renders overall information for [pipelines].
 * If [dragDropId] is provided, emits drag-drop payloads to it containing the selected [GameSystem].
 */
class PipelinesList(private val pipelines: Iterable<Pipeline>, private val dragDropId: String? = null) : Widget {
	private val history = History(10, 1000)
	private val historyWidth by lazy { -Style.spacing.x }

	private val currentSystem = DebouncedValue<GameSystem, Widget> { getWidget(GameSystem::class.java, it) }

	override fun invoke() {
		val historyHeight = max(Layout.lineHeight(4), Layout.free.y / 5)
		pipelines.forEachIndexed { i, pipeline ->
			var slowestSystem = "none"
			var slowestSystemTPS = 0L
			history("Pipeline $i", historyWidth, historyHeight) {
				pipeline.forEach { system ->
					val id = system.debugName
					val tps = system.sampler.value

					if (tps > slowestSystemTPS) {
						slowestSystemTPS = tps
						slowestSystem = id
					}

					line(id, tps.toDouble())

					dragDropId?.let { ddId ->
						onDragLegend(id) {
							setDragDropPayload(system, ddId)
							currentSystem.set(system)()
						}
					}

					legendTooltip(id) {
						text(system.joinToString { it.simpleName })
					}
				}

				dummy("*slowest")
				legendTooltip("*slowest") {
					text("%s (%s)".format(slowestSystem, MagFormat.seconds(slowestSystemTPS)))
				}
			}
		}
	}
}

