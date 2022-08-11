package dev.kkorolyov.pancake.graphics.gl.test.e2e

import dev.kkorolyov.pancake.graphics.PixelBuffer
import dev.kkorolyov.pancake.graphics.gl.image
import dev.kkorolyov.pancake.graphics.gl.resource.GLMesh
import dev.kkorolyov.pancake.graphics.gl.resource.GLProgram
import dev.kkorolyov.pancake.graphics.gl.resource.GLShader
import dev.kkorolyov.pancake.graphics.gl.resource.GLTexture
import dev.kkorolyov.pancake.graphics.gl.resource.GLVertexBuffer
import dev.kkorolyov.pancake.graphics.resource.Mesh
import dev.kkorolyov.pancake.graphics.resource.Program
import dev.kkorolyov.pancake.graphics.resource.VertexBuffer
import dev.kkorolyov.pancake.platform.Resources
import dev.kkorolyov.pancake.platform.math.Matrix4
import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.math.Vector3
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL46.*
import org.lwjgl.opengl.GLUtil
import org.lwjgl.system.MemoryStack

private val window by lazy {
	if (!glfwInit()) throw IllegalStateException("Cannot init GLFW")
	glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE)
	val window = glfwCreateWindow(640, 640, "graphics-gl test", 0, 0)
	if (window == 0L) throw IllegalStateException("Cannot create window")

	glfwMakeContextCurrent(window)
	glfwSwapInterval(1)

	GL.createCapabilities()
	GLUtil.setupDebugMessageCallback()
	glfwSetWindowSizeCallback(window) { _, width, height ->
		glViewport(0, 0, width, height)
	}

	window
}

private val colorProgram = GLProgram(
	GLShader(GLShader.Type.VERTEX, Resources.inStream("color.vert")),
	GLShader(GLShader.Type.FRAGMENT, Resources.inStream("color.frag"))
).apply {
	set(0, Matrix4.identity())
}
private val textureProgram = GLProgram(
	GLShader(GLShader.Type.VERTEX, Resources.inStream("texture.vert")),
	GLShader(GLShader.Type.FRAGMENT, Resources.inStream("texture.frag"))
).apply {
	set(0, Matrix4.identity())
}

private val texture = GLTexture {
	PixelBuffer.image(Resources.inStream("wall.jpg"))
}

private val solidVertices = Vector3.of(0.0, 0.5).let { color ->
	arrayOf(
		arrayOf(Vector2.of(-0.5, -0.5), color),
		arrayOf(Vector2.of(0.5, -0.5), color),
		arrayOf(Vector2.of(0.0, 0.5), color)
	)
}
private val rainbowVertices = arrayOf(
	arrayOf(Vector2.of(-0.5, -0.5), Vector3.of(1.0)),
	arrayOf(Vector2.of(0.5, -0.5), Vector3.of(0.0, 1.0)),
	arrayOf(Vector2.of(0.0, 0.5), Vector3.of(0.0, 0.0, 1.0))
)
private val textureVertices = arrayOf(
	arrayOf(Vector2.of(-0.5, -0.5), Vector2.of()),
	arrayOf(Vector2.of(0.5, -0.5), Vector2.of(1.0)),
	arrayOf(Vector2.of(0.0, 0.5), Vector2.of(0.5, 1.0))
)

private val solidBuffer: VertexBuffer = GLVertexBuffer(*solidVertices)
private val rainbowBuffer: VertexBuffer = GLVertexBuffer(*rainbowVertices)
private val textureBuffer: VertexBuffer = GLVertexBuffer(*textureVertices)

private val solidMesh = GLMesh(solidBuffer, mode = GLMesh.Mode.TRIANGLES)
private val rainbowMesh = GLMesh(rainbowBuffer, mode = GLMesh.Mode.TRIANGLES)
private val textureMesh = GLMesh(textureBuffer, mode = GLMesh.Mode.TRIANGLES, textures = listOf(texture))

private val scenes = mapOf(
	GLFW_KEY_1 to Scene(colorProgram, solidMesh),
	GLFW_KEY_2 to Scene(colorProgram, rainbowMesh),
	GLFW_KEY_3 to Scene(textureProgram, textureMesh)
)

private var currentScene = scenes.values.first()
private var currentScale = 1.0
private var currentX = 0.0
private var currentY = 0.0

private var dragX = 0.0
private var dragY = 0.0

fun main() {
	val vertBuffers = mapOf(
		solidVertices to solidBuffer,
		rainbowVertices to rainbowBuffer,
		textureVertices to textureBuffer
	)

	glfwSetKeyCallback(window) { _, key, _, action, _ ->
		if (action == GLFW_PRESS) {
			// test various meshes
			scenes[key]?.let {
				currentScene = it
			}

			// test ad-hoc vertex buffer changes
			if (key == GLFW_KEY_W) {
				vertBuffers.forEach { (vertices, buffer) ->
					vertices.forEach {
						it.forEach {
							it.scale(1.1)
						}
					}
					buffer.set(*vertices)
				}
			} else if (key == GLFW_KEY_S) {
				vertBuffers.forEach { (vertices, buffer) ->
					vertices.forEach {
						it.forEach {
							it.scale(0.9)
						}
					}
					buffer.set(*vertices)
				}
			}
		}
	}.use { }
	glfwSetScrollCallback(window) { _, _, yOffset ->
		transform(dScale = yOffset / 10)
	}.use { }
	glfwSetCursorPosCallback(window) { window, x, y ->
		if (glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS) {
			MemoryStack.stackPush().use { stack ->
				val widthP = stack.mallocInt(1)
				val heightP = stack.mallocInt(1)

				glfwGetWindowSize(window, widthP, heightP)

				// test uniforms
				transform(dX = (x - dragX) / widthP[0], dY = (y - dragY) / heightP[0])
			}
		}
		dragX = x
		dragY = y
	}.use { }

	while (!glfwWindowShouldClose(window)) {
		glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

		val (program, mesh) = currentScene
		program.activate()
		mesh.draw()

		glfwSwapBuffers(window)
		glfwPollEvents()
	}
}

private fun transform(dScale: Double = 0.0, dX: Double = 0.0, dY: Double = 0.0) {
	currentScale += dScale
	currentX += dX
	currentY += dY

	arrayOf(colorProgram, textureProgram).forEach {
		it[0] = Matrix4.identity().apply {
			scale(currentScale)
			xw = currentX * 2
			yw = -currentY * 2
		}
	}
}

private data class Scene(val program: Program, val mesh: Mesh)
