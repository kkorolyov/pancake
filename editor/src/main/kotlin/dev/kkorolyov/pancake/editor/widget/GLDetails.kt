package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.MagFormat
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.table
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.editor.tree
import imgui.flag.ImGuiTableFlags
import org.lwjgl.opengl.GL46.*
import org.lwjgl.opengl.GLUtil
import org.lwjgl.opengl.NVXGPUMemoryInfo
import java.io.ByteArrayOutputStream
import java.io.PrintStream

/**
 * Renders information about the current OpenGL implementation.
 */
class GLDetails : Widget {
	private val vendor = glGetString(GL_VENDOR) ?: ""
	private val renderer = glGetString(GL_RENDERER) ?: ""
	private val version = glGetString(GL_VERSION) ?: ""
	private val extensions = glGetString(GL_EXTENSIONS) ?: ""

	private val maxVertexAttributes = glGetInteger(GL_MAX_VERTEX_ATTRIBS)
	private val maxUniforms = glGetInteger(GL_MAX_UNIFORM_LOCATIONS)

	private val memTotal
		get() = MagFormat.bytes(glGetInteger(NVXGPUMemoryInfo.GL_GPU_MEMORY_INFO_TOTAL_AVAILABLE_MEMORY_NVX))
	private val memFree
		get() = MagFormat.bytes(glGetInteger(NVXGPUMemoryInfo.GL_GPU_MEMORY_INFO_CURRENT_AVAILABLE_VIDMEM_NVX))

	private val debugLog by lazy {
		val out = object : ByteArrayOutputStream() {
			override fun write(b: ByteArray, off: Int, len: Int) {
				if (count >= 10000) reset()
				super.write(b, off, len)
			}
		}
		GLUtil.setupDebugMessageCallback(PrintStream(out)).use {}

		Log(out)
	}

	private var wireframe = false

	override fun invoke() {
		table("details", 2, flags = ImGuiTableFlags.SizingStretchProp) {
			column { text("Vendor") }
			column { text(vendor) }

			column { text("Renderer") }
			column { text(renderer) }

			column { text("Version") }
			column { text(version) }

			column { text("Max vertex attributes") }
			column { text(maxVertexAttributes) }

			column { text("Max uniforms") }
			column { text(maxUniforms) }

			column { text("Total memory") }
			column { text(memTotal) }

			column { text("Free memory") }
			column { text(memFree) }
		}

		tree("Extensions") {
			list("##extensions") {
				extensions.split(" ").forEach {
					text(it)
				}
			}
		}

		tree("Debug") {
			debugLog()
		}

		input("wireframe", wireframe) {
			wireframe = it
			glPolygonMode(GL_FRONT_AND_BACK, if (wireframe) GL_LINE else GL_FILL)
		}
	}
}
