package dev.kkorolyov.pancake.core.editor

import dev.kkorolyov.pancake.core.animation.TransformFrame
import dev.kkorolyov.pancake.core.component.Animator
import dev.kkorolyov.pancake.editor.Layout
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.contextMenu
import dev.kkorolyov.pancake.editor.disabledIf
import dev.kkorolyov.pancake.editor.dragInput3
import dev.kkorolyov.pancake.editor.factory.WidgetFactory
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.editor.separator
import dev.kkorolyov.pancake.editor.sliderInput
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.editor.tree
import dev.kkorolyov.pancake.platform.entity.Component
import imgui.flag.ImGuiTreeNodeFlags

class AnimatorComponentWidgetFactory : WidgetFactory<Component> {
	override val type: Class<Component> = Component::class.java

	override fun get(t: Component): Widget? = WidgetFactory.get<Animator>(t) {
		var newOffset = 0
		val newKeyframe = TransformFrame()

		Widget {
			input("active", isActive) { isActive = it }
			maxByOrNull { it.value.playback.size() }?.value?.playback?.let { driverPlayback ->
				if (driverPlayback.size() >= 0) {
					Layout.width(Layout.stretchWidth) {
						sliderInput("##offset", driverPlayback.offset, min = 0, max = driverPlayback.size()) { newOffset ->
							forEach { (_, playbackConfig) ->
								playbackConfig.playback.offset = newOffset
							}
						}
					}
					tooltip("offset")
				}
			}

			forEach { (role, playbackConfig) ->
				val playback = playbackConfig.playback
				val type = playbackConfig.type

				tree("${role.key} (${type.name})", ImGuiTreeNodeFlags.SpanFullWidth) {
					disabledIf(true) { sliderInput("##offset-$role", playback.offset, min = 0, max = playback.size()) }

//					playback.timeline.map {  }
//
//					plot("timeline") {
//						scatter("keyframes", )
//					}

					val frameIt = playback.timeline.iterator()
					while (frameIt.hasNext()) {
						val (offset, keyframe) = frameIt.next()

						// draw context menu EITHER in open treenode or in closed state
						if (
							!tree(offset.toString(), ImGuiTreeNodeFlags.SpanFullWidth) {
								contextMenu {
									menuItem("remove") { frameIt.remove() }
								}
								drawTransformFrame(keyframe)
							}
						) {
							contextMenu {
								menuItem("remove") { frameIt.remove() }
							}
						}
					}

					separator()
					tree("add keyframe") {
						Layout.width(Layout.stretchWidth) {
							input("##offset", newOffset) { newOffset = it }
							tooltip("offset")

							drawTransformFrame(newKeyframe)

							button("add") {
								playback.timeline.put(newOffset, TransformFrame().apply {
									translation.set(newKeyframe.translation)
									rotation.set(newKeyframe.rotation)
									scale.set(newKeyframe.scale)
								})
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
