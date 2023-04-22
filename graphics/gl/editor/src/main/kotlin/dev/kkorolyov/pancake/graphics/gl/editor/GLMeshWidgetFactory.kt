package dev.kkorolyov.pancake.graphics.gl.editor

import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.factory.WidgetFactory
import dev.kkorolyov.pancake.editor.getValue
import dev.kkorolyov.pancake.editor.image
import dev.kkorolyov.pancake.graphics.PixelBuffer
import dev.kkorolyov.pancake.graphics.gl.resource.GLFrameBuffer
import dev.kkorolyov.pancake.graphics.gl.resource.GLMesh
import dev.kkorolyov.pancake.graphics.gl.resource.GLProgram
import dev.kkorolyov.pancake.graphics.gl.resource.GLShader
import dev.kkorolyov.pancake.graphics.gl.resource.GLTexture
import dev.kkorolyov.pancake.graphics.resource.Mesh
import dev.kkorolyov.pancake.graphics.scoped
import dev.kkorolyov.pancake.platform.io.Resources
import dev.kkorolyov.pancake.platform.math.Matrix4
import dev.kkorolyov.pancake.platform.math.Vector3
import org.lwjgl.opengl.GL46.*
import org.lwjgl.system.MemoryUtil

private const val width = 128
private const val height = 128

private val tProgram by ThreadLocal.withInitial {
	GLProgram(
		Resources.inStream("dev/kkorolyov/pancake/graphics/gl/editor/shaders/image.vert").use { GLShader(GLShader.Type.VERTEX, it) },
		Resources.inStream("dev/kkorolyov/pancake/graphics/gl/editor/shaders/image.frag").use { GLShader(GLShader.Type.FRAGMENT, it) }
	) {
		// pancake meshes are defined on a [-0.5,0.5] scale; double up to the OpenGL [-1,1] scale
		set(0, Matrix4.identity().apply {
			scale(Vector3.of(2.0, 2.0))
		})
	}
}
private val tTexture by ThreadLocal.withInitial {
	GLTexture {
		PixelBuffer(width, height, 0, 4, MemoryUtil.memAlloc(0), MemoryUtil::memFree)
	}
}
private val tFrameBuffer by ThreadLocal.withInitial { GLFrameBuffer(tTexture) }

class GLMeshWidgetFactory : WidgetFactory<Mesh> {
	override val type = Mesh::class.java

	override fun get(t: Mesh): Widget? = WidgetFactory.get<GLMesh>(t) {
		Widget {
			val program = tProgram
			val texture = tTexture
			val frameBuffer = tFrameBuffer

			frameBuffer.scoped {
				glViewport(0, 0, width, height)
				glClear(GL_COLOR_BUFFER_BIT)

				program.scoped { draw() }
			}

			image(texture, width.toFloat(), height.toFloat())
		}
	}

	override fun get(c: Class<Mesh>, onNew: (Mesh) -> Unit): Widget? {
		TODO("Not yet implemented")
	}
}
