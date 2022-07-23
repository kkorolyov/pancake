package dev.kkorolyov.pancake.editor.test

import dev.kkorolyov.pancake.editor.Container
import dev.kkorolyov.pancake.editor.widget.Editor
import dev.kkorolyov.pancake.editor.widget.Window
import dev.kkorolyov.pancake.platform.GameEngine
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.Pipeline
import dev.kkorolyov.pancake.platform.Resources
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL46

private const val editorSettings = "editor.ini"

private val window = run {
	if (!GLFW.glfwInit()) throw IllegalStateException("Cannot init GLFW")
	GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE)
	val window = GLFW.glfwCreateWindow(640, 640, "Editor Test", 0, 0)
	if (window == 0L) throw IllegalStateException("Cannot create window")

	GLFW.glfwMakeContextCurrent(window)
	GLFW.glfwSwapInterval(1)
	GLFW.glfwShowWindow(window)

	GL.createCapabilities()

	window
}

private val container = Container(window).apply {
	Resources.inStream(editorSettings)?.use(::load)
}
private lateinit var editor: Window

/**
 * Returns a system setting up GL context to draw frame.
 */
fun drawStart(): GameSystem = Pipeline.run {
	GLFW.glfwMakeContextCurrent(window)
	GL46.glClear(GL46.GL_COLOR_BUFFER_BIT or GL46.GL_DEPTH_BUFFER_BIT)
}

/**
 * Returns a system swapping frame buffers and polling input events.
 */
fun drawEnd(): GameSystem = Pipeline.run {
	GLFW.glfwSwapBuffers(window)
	GLFW.glfwPollEvents()
}

/**
 * Returns a system rendering an editor for [engine].
 */
fun editor(engine: GameEngine): GameSystem {
	editor = Window("Editor", Editor(engine))
	return Pipeline.run {
		container(editor)
	}
}

/**
 * Starts a window running [engine].
 */
fun start(engine: GameEngine) {
	GLFW.glfwSetWindowCloseCallback(window) {
		engine.stop()
	}.use { }

	engine.start()

	Resources.outStream(editorSettings)?.use(container::close) ?: container.close()
}
