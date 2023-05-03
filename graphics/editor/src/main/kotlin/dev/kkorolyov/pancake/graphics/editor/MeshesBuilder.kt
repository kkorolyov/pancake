package dev.kkorolyov.pancake.graphics.editor

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.disabledIf
import dev.kkorolyov.pancake.editor.factory.getWidget
import dev.kkorolyov.pancake.editor.image
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.separator
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.editor.widget.Modal
import dev.kkorolyov.pancake.graphics.editor.factory.getSnapshot
import dev.kkorolyov.pancake.graphics.resource.Mesh

private const val IMAGE_WIDTH = 128
private const val IMAGE_HEIGHT = 128

/**
 * Draws a piecewise [c]-type mesh selection wizard, invoking [onConfirm] when the current state is accepted.
 */
class MeshesBuilder(private val c: Class<out Mesh>, private val onConfirm: (List<Mesh>) -> Unit) : Widget {
	// TODO this only closes on apply - not close
	private val snapshot = getSnapshot(c, IMAGE_WIDTH, IMAGE_HEIGHT)
	private val meshes = mutableListOf<Mesh>()

	private var create: Modal? = null

	override fun invoke() {
		text("IMPLEMENT ME")

		list("##meshes") {
			if (meshes.isNotEmpty()) {
				meshes.forEach {
					image(snapshot(listOf(it)), IMAGE_WIDTH.toFloat(), IMAGE_HEIGHT.toFloat())
				}

				separator()
			}

			disabledIf(true) {
				button("+") {
					create = Modal("New mesh", getWidget(Mesh::class.java, c) {
						create?.visible = false
						meshes += it
					})
				}
			}
		}

		disabledIf(true) {
			button("apply") {
				snapshot.close()
				onConfirm(meshes)
			}
		}

		create?.invoke()
	}
}
