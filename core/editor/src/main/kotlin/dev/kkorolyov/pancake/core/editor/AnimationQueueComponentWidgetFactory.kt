package dev.kkorolyov.pancake.core.editor

import dev.kkorolyov.pancake.core.animation.TransformFrame
import dev.kkorolyov.pancake.core.component.AnimationQueue
import dev.kkorolyov.pancake.editor.Layout
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.contextMenu
import dev.kkorolyov.pancake.editor.disabledIf
import dev.kkorolyov.pancake.editor.dragInput3
import dev.kkorolyov.pancake.editor.factory.WidgetFactory
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.editor.onActive
import dev.kkorolyov.pancake.editor.separator
import dev.kkorolyov.pancake.editor.sliderInput
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.editor.tree
import dev.kkorolyov.pancake.platform.entity.Component
import imgui.flag.ImGuiTreeNodeFlags

class AnimationQueueComponentWidgetFactory : WidgetFactory<Component> {
	override val type: Class<Component> = Component::class.java

	override fun get(t: Component): Widget? = WidgetFactory.get<AnimationQueue>(t) {
		var lastActive = false

		var newOffset = 0
		val newKeyframe = TransformFrame()

		Widget {
			val driverPlayback = maxByOrNull { it.playback.size() }?.playback
			driverPlayback?.let {
				Layout.width(Layout.stretchWidth) {
					sliderInput("##offset", it.offset, min = 0, max = it.size())
				}
				tooltip("offset")

				onActive(
					{
						lastActive = isActive
						isActive = false
					},
					{ isActive = lastActive }
				)
			}

			forEachIndexed { playbackConfigI, playbackConfig ->
				val playback = playbackConfig.playback
				val type = playbackConfig.type

				tree("$playbackConfigI (${type.name})", ImGuiTreeNodeFlags.SpanFullWidth) {
					disabledIf(true) { sliderInput("##offset-$playbackConfigI", playback.offset, min = 0, max = playback.size()) }

					// FIXME adding/removing frames during playback crops up problematic behavior - for obvious reasons

					val frameIt = playback.timeline.iterator()
					while (frameIt.hasNext()) {
						val (offset, keyframe) = frameIt.next()

						tree(offset.toString(), ImGuiTreeNodeFlags.SpanFullWidth) {
							contextMenu {
								menuItem("remove") { frameIt.remove() }
							}
							drawTransformFrame(keyframe)
						}
					}

					separator()
					tree("add keyframe") {
						Layout.width(Layout.stretchWidth) {
							input("##offset", newOffset) { newOffset = it }
							tooltip("offset")

							drawTransformFrame(newKeyframe)

							button("add") {
								playback.timeline.put(newOffset, TransformFrame(newKeyframe.translation, newKeyframe.rotation, newKeyframe.scale))
							}
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
