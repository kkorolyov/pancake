package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.ext.ptr
import dev.kkorolyov.pancake.editor.field
import dev.kkorolyov.pancake.editor.header
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.text
import imgui.ImGui
import org.lwjgl.opengl.GL46.*
import org.lwjgl.opengl.GLUtil
import org.lwjgl.opengl.NVXGPUMemoryInfo
import java.io.ByteArrayOutputStream
import java.io.PrintStream

private const val kib = 1 shl 10
private const val mib = kib shl 10
private const val gib = mib shl 10

private fun prettyMem(kbs: Int): String =
	if (kbs > gib) prettyMem(kbs.toDouble() / gib, 'G')
	else if (kbs > mib) prettyMem(kbs.toDouble() / mib, 'M')
	else prettyMem(kbs.toDouble(), 'K')

private fun prettyMem(value: Double, unit: Char) = String.format("%.2f%siB", value, unit)

/**
 * Renders information about the current OpenGL implementation.
 */
class GLDetails : Widget {
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

	private val memTotal by lazy {
		prettyMem(glGetInteger(NVXGPUMemoryInfo.GL_GPU_MEMORY_INFO_TOTAL_AVAILABLE_MEMORY_NVX))
	}
	private val memFree
		get() = prettyMem(glGetInteger(NVXGPUMemoryInfo.GL_GPU_MEMORY_INFO_CURRENT_AVAILABLE_VIDMEM_NVX))

	private val debugLog by lazy {
		val out = object : ByteArrayOutputStream() {
			override fun write(b: ByteArray, off: Int, len: Int) {
				if (count >= mib) reset()
				super.write(b, off, len)
			}
		}
		GLUtil.setupDebugMessageCallback(PrintStream(out))

		Log(out)
	}

	private val wireframePtr = false.ptr()

	override fun invoke() {
		field("Vendor", vendor)
		field("Renderer", renderer)
		field("Version", version)

		header("Extensions") {
			list("##extensions") {
				extensions?.split(" ")?.forEach {
					text(it)
				}
			}
		}

		field("Max vertex attributes", maxVertexAttributes)
		field("Max uniforms", maxUniforms)

		field("Total memory", memTotal)
		field("Free memory", memFree)

		header("Debug") {
			debugLog()
		}

		ImGui.checkbox("wireframe", wireframePtr)

		glPolygonMode(GL_FRONT_AND_BACK, if (wireframePtr.get()) GL_LINE else GL_FILL)
	}
}
