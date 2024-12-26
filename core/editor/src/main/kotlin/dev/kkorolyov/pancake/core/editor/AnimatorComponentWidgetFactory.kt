package dev.kkorolyov.pancake.core.editor

import dev.kkorolyov.pancake.core.animation.TransformFrame
import dev.kkorolyov.pancake.core.component.Animator
import dev.kkorolyov.pancake.editor.DebouncedValue
import dev.kkorolyov.pancake.editor.Mouse
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.contextMenu
import dev.kkorolyov.pancake.editor.dragInput3
import dev.kkorolyov.pancake.editor.factory.WidgetFactory
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.editor.onClick
import dev.kkorolyov.pancake.editor.onHover
import dev.kkorolyov.pancake.editor.sequencer
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.editor.widget.Popup
import dev.kkorolyov.pancake.platform.entity.Component
import imgui.flag.ImGuiMouseButton
import kotlin.math.log10
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sign

class AnimatorComponentWidgetFactory : WidgetFactory<Component> {
	override val type: Class<Component> = Component::class.java

	override fun get(t: Component): Widget? = WidgetFactory.get<Animator>(t) {
		var min = 0
		var max = 0

		val currentKeyframe = DebouncedValue<TransformFrame, Widget> { Widget { drawTransformFrame(it) } }
		val inlineDetails = Popup("keyframeDetails")

		Widget {
			if (min == 0 && max == 0) {
				max = duration()
			}

			input("active", isActive) { isActive = it }

			sequencer("##value", min, max, max(2, 10.0.pow(log10((max - min).toDouble()).roundToInt() - 1).roundToInt())) {
				marker(0)
				marker(duration())

				onHover {
					Mouse.onScroll {
						val step = 0.1 * (max - min)
						val minDelta = mouseRatio * step
						val maxDelta = step - minDelta

						min = (min + minDelta * it.sign).roundToInt()
						max = (max - maxDelta * it.sign).roundToInt()
					}
					Mouse.onClick(ImGuiMouseButton.Middle) {
						min = 0
						max = duration()
					}
				}

				scrubber(offset) {
					offset = it
				}

				forEach { (role, playbackConfig) ->
					val timeline = playbackConfig.playback.timeline

					track(role.key) {
						var clickedOffset: Int? = null
						onClick {
							clickedOffset = mouseOffset
							if (clickedOffset!! < 0) clickedOffset = null
						}

						var moveFrame: Pair<Int, Int>? = null

						var frameI = 0
						val timelineIt = timeline.iterator()
						timelineIt.forEachRemaining { (frameOffset, frame) ->
							keyframe(frameI++.toString(), frameOffset) {
								if (it >= 0 && it !in timeline) {
									moveFrame = frameOffset to it
								}
							}
							tooltip(frameOffset)
							onClick {
								inlineDetails.open(currentKeyframe.set(frame))
								// preempt any track click
								clickedOffset = null
							}
							contextMenu {
								menuItem("remove") {
									timelineIt.remove()
								}
							}
						}

						// while moving, don't draw popup
						moveFrame?.let { (from, to) ->
							timeline.put(to, timeline.remove(from))
						} ?: inlineDetails()

						clickedOffset?.let {
							timeline.put(it, TransformFrame())
						}
					}
				}
			}
		}
	}

	private fun drawTransformFrame(frame: TransformFrame) {
		dragInput3("##translation", frame.translation, onChange = frame.translation::set)
		tooltip("translation")

		dragInput3("##rotation", frame.rotation, onChange = frame.rotation::set)
		tooltip("rotation")

		dragInput3("##scale", frame.scale, onChange = frame.scale::set)
		tooltip("scale")
	}

	override fun get(c: Class<out Component>, onNew: (Component) -> Unit): Widget? {
		TODO("Not yet implemented")
	}
}
