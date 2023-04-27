package dev.kkorolyov.pancake.graphics.gl.editor

import dev.kkorolyov.pancake.graphics.PixelBuffer
import dev.kkorolyov.pancake.graphics.editor.Snapshot
import dev.kkorolyov.pancake.graphics.gl.resource.GLFrameBuffer
import dev.kkorolyov.pancake.graphics.gl.resource.GLProgram
import dev.kkorolyov.pancake.graphics.gl.resource.GLShader
import dev.kkorolyov.pancake.graphics.gl.resource.GLTexture
import dev.kkorolyov.pancake.graphics.resource.Mesh
import dev.kkorolyov.pancake.graphics.resource.Texture
import dev.kkorolyov.pancake.graphics.scoped
import dev.kkorolyov.pancake.platform.io.Resources
import dev.kkorolyov.pancake.platform.math.Matrix4
import dev.kkorolyov.pancake.platform.math.Vector3
import org.lwjgl.opengl.GL46.*
import org.lwjgl.system.MemoryUtil

private val program by lazy {
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

/**
 * [Snapshot] accepting OpenGL meshes.
 */
class GLSnapshot(private val width: Int, private val height: Int) : Snapshot {
	private val texture = GLTexture {
		PixelBuffer(width, height, 0, 4, MemoryUtil.memAlloc(0), MemoryUtil::memFree)
	}
	private val frameBuffer = GLFrameBuffer(texture)

	override fun invoke(meshes: List<Mesh>): Texture {
		frameBuffer.scoped {
			glViewport(0, 0, width, height)
			glClear(GL_COLOR_BUFFER_BIT)

			program.scoped {
				meshes.forEach { it.draw() }
			}
		}

		return texture
	}

	override fun close() {
		texture.close()
		frameBuffer.close()
	}
}
