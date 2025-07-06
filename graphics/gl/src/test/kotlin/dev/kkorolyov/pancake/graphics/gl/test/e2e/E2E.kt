package dev.kkorolyov.pancake.graphics.gl.test.e2e

import dev.kkorolyov.pancake.graphics.Font
import dev.kkorolyov.pancake.graphics.ellipse
import dev.kkorolyov.pancake.graphics.gl.GLRenderBackend
import dev.kkorolyov.pancake.graphics.image
import dev.kkorolyov.pancake.graphics.rectangle
import dev.kkorolyov.pancake.graphics.resource.Buffer
import dev.kkorolyov.pancake.graphics.resource.Mesh
import dev.kkorolyov.pancake.graphics.resource.Program
import dev.kkorolyov.pancake.graphics.resource.Shader
import dev.kkorolyov.pancake.graphics.resource.Texture
import dev.kkorolyov.pancake.graphics.resource.Vertex
import dev.kkorolyov.pancake.platform.io.Resources
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

private val renderBackend = GLRenderBackend()

private val colorProgram = Program(
	Shader(Shader.Type.VERTEX, Shader.Source.Path("color.vert")),
	Shader(Shader.Type.FRAGMENT, Shader.Source.Path("color.frag"))
).apply {
	uniforms[0] = Matrix4.of()
}
private val textureProgram = Program(
	Shader(Shader.Type.VERTEX, Shader.Source.Path("texture.vert")),
	Shader(Shader.Type.FRAGMENT, Shader.Source.Path("texture.frag"))
).apply {
	uniforms[0] = Matrix4.of()
}
private val coTexProgram = Program(
	Shader(Shader.Type.VERTEX, Shader.Source.Path("coTex.vert")),
	Shader(Shader.Type.FRAGMENT, Shader.Source.Path("coTex.frag"))
).apply {
	uniforms[0] = Matrix4.of()
}
private val fontProgram = Program(
	Shader(Shader.Type.VERTEX, Shader.Source.Path("texture.vert")),
	Shader(Shader.Type.FRAGMENT, Shader.Source.Path("font.frag"))
).apply {
	uniforms[0] = Matrix4.of()
}

private val texture = Texture {
	Resources.inStream("wall.jpg").use(::image)
}

private val solidBuffer = Buffer.vertex(*Vector3.of(0.0, 0.5).let { color ->
	arrayOf(
		Vertex(Vector2.of(-0.5, -0.5), color),
		Vertex(Vector2.of(0.5, -0.5), color),
		Vertex(Vector2.of(0.0, 0.5), color)
	)
})
private val rainbowBuffer = Buffer.vertex(
	Vertex(Vector2.of(-0.5, -0.5), Vector3.of(1.0)),
	Vertex(Vector2.of(0.5, -0.5), Vector3.of(0.0, 1.0)),
	Vertex(Vector2.of(0.0, 0.5), Vector3.of(0.0, 0.0, 1.0))
)
private val textureBuffer = Buffer.vertex(
	Vertex(Vector2.of(-0.5, -0.5), Vector2.of()),
	Vertex(Vector2.of(0.5, -0.5), Vector2.of(1.0)),
	Vertex(Vector2.of(0.0, 0.5), Vector2.of(0.5, 1.0))
)
private val coTexBuffer = Buffer.vertex(
	Vertex(Vector2.of(-0.5, -0.5), Vector2.of(), Vector3.of(0.0, 0.0, 1.0)),
	Vertex(Vector2.of(0.5, -0.5), Vector2.of(1.0), Vector3.of(0.0, 1.0)),
	Vertex(Vector2.of(0.0, 0.5), Vector2.of(0.5, 1.0), Vector3.of(1.0))
)

private val solidMesh = Mesh(solidBuffer, mode = Mesh.Mode.TRIANGLES)
private val rainbowMesh = Mesh(rainbowBuffer, mode = Mesh.Mode.TRIANGLES)
private val textureMesh = Mesh(textureBuffer, textures = listOf(texture), mode = Mesh.Mode.TRIANGLES)
private val coTexMesh = Mesh(coTexBuffer, textures = listOf(texture), mode = Mesh.Mode.TRIANGLES)

private val rectangleMesh = Mesh(
	Buffer.vertex().apply {
		rectangle(Vector2.of(1.0, 1.0)) { position, texCoord ->
			add(Vertex(position, texCoord))
		}
	},
	textures = listOf(texture),
	mode = Mesh.Mode.TRIANGLE_FAN
)
private val ellipseMesh = Mesh(
	Buffer.vertex().apply {
		ellipse(Vector2.of(1.0, 1.0)) { position, texCoord ->
			add(Vertex(position, texCoord))
		}
	},
	textures = listOf(texture),
	mode = Mesh.Mode.TRIANGLE_FAN
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
	Scene(
		fontProgram,
		font(
			(0 until 6).joinToString("\n") { row ->
				val columns = 16
				val offset = row * columns + 32

				(offset until offset + columns).map(::Char).joinToString("") { it.toString() }
			}
		)
	)
)

private var wireframe = false

private var currentScene = scenes.first()
private var currentScale = 1.0
private var currentX = 0.0
private var currentY = 0.0

private var dragX = 0.0
private var dragY = 0.0

fun main() {
	val vertBuffers = listOf(
		solidBuffer,
		rainbowBuffer,
		textureBuffer
	)

	glfwSetKeyCallback(window) { _, key, _, action, _ ->
		if (action == GLFW_PRESS || action == GLFW_REPEAT) {
			// test various meshes
			val sceneI = key - GLFW_KEY_1
			if (sceneI >= 0 && sceneI < scenes.size) {
				currentScene = scenes[sceneI]
			}

			// test ad-hoc vertex buffer changes
			if (key == GLFW_KEY_W) {
				vertBuffers.forEach { buffer ->
					buffer.forEachIndexed { i, vertex ->
						buffer[i] = vertex.augment { it.scale(1.1) }
					}
				}
			} else if (key == GLFW_KEY_S) {
				vertBuffers.forEach { buffer ->
					buffer.forEachIndexed { i, vertex ->
						buffer[i] = vertex.augment { it.scale(0.9) }
					}
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
		renderBackend.draw(program, listOf(mesh))

		glfwSwapBuffers(window)
		glfwPollEvents()
	}
}

private fun transform(dScale: Double = 0.0, dX: Double = 0.0, dY: Double = 0.0) {
	currentScale += dScale
	currentX += dX
	currentY += dY

	scenes.forEach { (program, _) ->
		program.uniforms[0] = Matrix4.of().apply {
			scale(currentScale)
			ww = 1.0
			xw = currentX * 2
			yw = -currentY * 2
		}
	}
}

private data class Scene(val program: Program, val mesh: Mesh)

inline fun Vertex.augment(op: (Vector2) -> Unit): Vertex = Vertex(*this.map {
	(when (it) {
		is Vector3 -> Vector3.of(it)
		else -> Vector2.of(it)
	}).apply { op(this) }
}.toTypedArray())
