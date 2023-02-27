package dev.kkorolyov.pancake.graphics.gl.test.e2e

import dev.kkorolyov.pancake.graphics.ellipse
import dev.kkorolyov.pancake.graphics.gl.Font
import dev.kkorolyov.pancake.graphics.gl.image
import dev.kkorolyov.pancake.graphics.gl.resource.GLMesh
import dev.kkorolyov.pancake.graphics.gl.resource.GLProgram
import dev.kkorolyov.pancake.graphics.gl.resource.GLShader
import dev.kkorolyov.pancake.graphics.gl.resource.GLTexture
import dev.kkorolyov.pancake.graphics.gl.resource.GLVertexBuffer
import dev.kkorolyov.pancake.graphics.rectangle
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

private val helpText = """
	graphics-gl E2E test suite
	
	1-9 - scene select
	W,S - scale through vertex buffer manipulation
	F - toggle wireframe mode
	mouse click + drag - translate/pan through shader uniform
	mouse scroll - scale through shader uniform
""".trimIndent()

private val window by lazy {
	if (!glfwInit()) throw IllegalStateException("Cannot init GLFW")
	glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE)
	val window = glfwCreateWindow(640, 640, "graphics-gl test", 0, 0)
	if (window == 0L) throw IllegalStateException("Cannot create window")

	glfwMakeContextCurrent(window)
	glfwSwapInterval(1)

	GL.createCapabilities()
	GLUtil.setupDebugMessageCallback()
	glfwSetFramebufferSizeCallback(window) { _, width, height ->
		glViewport(0, 0, width, height)
	}

	glEnable(GL_BLEND)
	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

	window
}

private val colorProgram = GLProgram(
	Resources.inStream("color.vert").use { GLShader(GLShader.Type.VERTEX, it) },
	Resources.inStream("color.frag").use { GLShader(GLShader.Type.FRAGMENT, it) }
) {
	set(0, Matrix4.identity())
}
private val textureProgram = GLProgram(
	Resources.inStream("texture.vert").use { GLShader(GLShader.Type.VERTEX, it) },
	Resources.inStream("texture.frag").use { GLShader(GLShader.Type.FRAGMENT, it) }
) {
	set(0, Matrix4.identity())
}
private val coTexProgram = GLProgram(
	Resources.inStream("coTex.vert").use { GLShader(GLShader.Type.VERTEX, it) },
	Resources.inStream("coTex.frag").use { GLShader(GLShader.Type.FRAGMENT, it) }
) {
	set(0, Matrix4.identity())
}
private val fontProgram = GLProgram(
	Resources.inStream("texture.vert").use { GLShader(GLShader.Type.VERTEX, it) },
	Resources.inStream("font.frag").use { GLShader(GLShader.Type.FRAGMENT, it) }
) {
	set(0, Matrix4.identity())
}

private val texture = GLTexture {
	Resources.inStream("wall.jpg").use(::image)
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
private val coTexVertices = arrayOf(
	arrayOf(Vector2.of(-0.5, -0.5), Vector2.of(), Vector3.of(0.0, 0.0, 1.0)),
	arrayOf(Vector2.of(0.5, -0.5), Vector2.of(1.0), Vector3.of(0.0, 1.0)),
	arrayOf(Vector2.of(0.0, 0.5), Vector2.of(0.5, 1.0), Vector3.of(1.0))
)

private val solidBuffer: VertexBuffer = GLVertexBuffer(*solidVertices)
private val rainbowBuffer: VertexBuffer = GLVertexBuffer(*rainbowVertices)
private val textureBuffer: VertexBuffer = GLVertexBuffer(*textureVertices)
private val coTexBuffer = GLVertexBuffer(*coTexVertices)

private val solidMesh = GLMesh(solidBuffer, mode = GLMesh.Mode.TRIANGLES)
private val rainbowMesh = GLMesh(rainbowBuffer, mode = GLMesh.Mode.TRIANGLES)
private val textureMesh = GLMesh(textureBuffer, mode = GLMesh.Mode.TRIANGLES, textures = listOf(texture))
private val coTexMesh = GLMesh(coTexBuffer, mode = GLMesh.Mode.TRIANGLES, textures = listOf(texture))

private val rectangleMesh = GLMesh(
	GLVertexBuffer {
		rectangle(Vector2.of(1.0, 1.0)) { position, texCoord ->
			add(position, texCoord)
		}
	},
	mode = GLMesh.Mode.TRIANGLE_FAN,
	textures = listOf(texture)
)
private val ellipseMesh = GLMesh(
	GLVertexBuffer {
		ellipse(Vector2.of(1.0, 1.0)) { position, texCoord ->
			add(position, texCoord)
		}
	},
	mode = GLMesh.Mode.TRIANGLE_FAN,
	textures = listOf(texture)
)

private val font = Resources.inStream("roboto-mono.ttf").use { Font(it, 14) }

private val scenes = listOf(
	// help
	Scene(fontProgram, font(helpText)),
	// triangles
	Scene(colorProgram, solidMesh),
	Scene(colorProgram, rainbowMesh),
	Scene(textureProgram, textureMesh),
	// other shapes
	Scene(textureProgram, rectangleMesh),
	Scene(textureProgram, ellipseMesh),
	// speshul
	Scene(coTexProgram, coTexMesh),
	Scene(fontProgram, font(
		(0 until 6).joinToString("\n") { row ->
			val columns = 16
			val offset = row * columns + 32

			(offset until offset + columns).map(::Char).joinToString("") { it.toString() }
		}
	))
)

private var wireframe = false

private var currentScene = scenes.first()
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
			val sceneI = key - GLFW_KEY_1
			if (sceneI >= 0 && sceneI < scenes.size) {
				currentScene = scenes[sceneI]
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

			// show wireframes
			if (key == GLFW_KEY_F) {
				wireframe = !wireframe
				glPolygonMode(GL_FRONT_AND_BACK, if (wireframe) GL_LINE else GL_FILL)
			}
		}
	}.use { }

	// test uniforms
	glfwSetScrollCallback(window) { _, _, yOffset ->
		transform(dScale = yOffset / 10)
	}.use { }
	glfwSetCursorPosCallback(window) { window, x, y ->
		if (glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS) {
			MemoryStack.stackPush().use { stack ->
				val widthP = stack.mallocInt(1)
				val heightP = stack.mallocInt(1)

				glfwGetFramebufferSize(window, widthP, heightP)

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

	scenes.forEach { (program, _) ->
		program[0] = Matrix4.identity().apply {
			scale(currentScale)
			ww = 1.0
			xw = currentX * 2
			yw = -currentY * 2
		}
	}
}

private data class Scene(val program: Program, val mesh: Mesh)
