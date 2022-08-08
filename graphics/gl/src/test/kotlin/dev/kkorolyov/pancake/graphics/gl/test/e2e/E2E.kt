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
import dev.kkorolyov.pancake.platform.Resources
import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.math.Vector3
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL46.*

private val window = run {
	if (!glfwInit()) throw IllegalStateException("Cannot init GLFW")
	val window = glfwCreateWindow(640, 640, "graphics-gl test", 0, 0)
	if (window == 0L) throw IllegalStateException("Cannot create window")

	glfwMakeContextCurrent(window)
	glfwSwapInterval(1)

	GL.createCapabilities()

	glfwSetWindowSizeCallback(window) { _, width, height ->
		glViewport(0, 0, width, height)
	}

	window
}

private val colorProgram = GLProgram(
	GLShader(GLShader.Type.VERTEX, Resources.inStream("color.vert")),
	GLShader(GLShader.Type.FRAGMENT, Resources.inStream("color.frag"))
)
private val textureProgram = GLProgram(
	GLShader(GLShader.Type.VERTEX, Resources.inStream("texture.vert")),
	GLShader(GLShader.Type.FRAGMENT, Resources.inStream("texture.frag"))
)

private val texture = GLTexture {
	PixelBuffer.image(Resources.inStream("wall.jpg"))
}

private val solidMesh = GLMesh(
	GLVertexBuffer {
		val color = Vector3.of(0.0, 0.5)

		add(Vector2.of(-0.5, -0.5), color)
		add(Vector2.of(0.5, -0.5), color)
		add(Vector2.of(0.0, 0.5), color)
	},
	mode = GLMesh.Mode.TRIANGLES
)
private val rainbowMesh = GLMesh(
	GLVertexBuffer {
		add(Vector2.of(-0.5, -0.5), Vector3.of(1.0))
		add(Vector2.of(0.5, -0.5), Vector3.of(0.0, 1.0))
		add(Vector2.of(0.0, 0.5), Vector3.of(0.0, 0.0, 1.0))
	},
	mode = GLMesh.Mode.TRIANGLES
)
private val textureMesh = GLMesh(
	GLVertexBuffer {
		add(Vector2.of(-0.5, -0.5), Vector2.of())
		add(Vector2.of(0.5, -0.5), Vector2.of(1.0))
		add(Vector2.of(0.0, 0.5), Vector2.of(0.5, 1.0))
	},
	mode = GLMesh.Mode.TRIANGLES,
	textures = listOf(texture)
)

private val scenes = mapOf(
	GLFW_KEY_1 to Scene(colorProgram, solidMesh),
	GLFW_KEY_2 to Scene(colorProgram, rainbowMesh),
	GLFW_KEY_3 to Scene(textureProgram, textureMesh)
)
private var scene = scenes.values.first()

fun main() {
	glfwSetKeyCallback(window) { _, key, _, action, _ ->
		if (action == GLFW_PRESS) {
			scenes[key]?.let {
				scene = it
			}
		}
	}.use { }

	while (!glfwWindowShouldClose(window)) {
		glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

		val (program, mesh) = scene
		program.activate()
		mesh.draw()

		glfwSwapBuffers(window)
		glfwPollEvents()
	}
}

private data class Scene(val program: Program, val mesh: Mesh)
