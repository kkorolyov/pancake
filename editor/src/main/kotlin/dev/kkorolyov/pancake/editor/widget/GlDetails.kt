package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.ext.ptr
import dev.kkorolyov.pancake.editor.field
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.text
import imgui.ImGui
import org.lwjgl.opengl.GL46.*

/**
 * Renders information about the current OpenGL implementation.
 */
class GlDetails : Widget {
	private val vendor by lazy {
		glGetString(GL_VENDOR)
	}
	private val renderer by lazy {
		glGetString(GL_RENDERER)
	}
	private val version by lazy {
		glGetString(GL_VERSION)
	}
	private val extensions by lazy {
		glGetString(GL_EXTENSIONS)
	}

	private val maxVertexAttributes by lazy {
		glGetInteger(GL_MAX_VERTEX_ATTRIBS)
	}
	private val maxUniforms by lazy {
		glGetInteger(GL_MAX_UNIFORM_LOCATIONS)
	}

	private val wireframePtr = false.ptr()

	override fun invoke() {
		field("Vendor", vendor)
		field("Renderer", renderer)
		field("Version", version)
		text("Extensions")
		list("##extensions") {
			extensions?.split(" ")?.forEach {
				text(it)
			}
		}

		field("Max vertex attributes", maxVertexAttributes)
		field("Max uniforms", maxUniforms)

		ImGui.checkbox("wireframe", wireframePtr)

		glPolygonMode(GL_FRONT_AND_BACK, if (wireframePtr.get()) GL_LINE else GL_FILL)
	}
}
