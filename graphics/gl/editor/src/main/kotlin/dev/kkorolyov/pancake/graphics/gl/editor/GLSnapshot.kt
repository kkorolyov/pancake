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
import kotlin.math.max

// width and height of the snapshot viewport
private const val RESOLUTION = 512

private val transform = Matrix4.of()
private val program by lazy {
	GLProgram(
		Resources.inStream("dev/kkorolyov/pancake/graphics/gl/editor/shaders/image.vert").use { GLShader(GLShader.Type.VERTEX, it) },
		Resources.inStream("dev/kkorolyov/pancake/graphics/gl/editor/shaders/image.frag").use { GLShader(GLShader.Type.FRAGMENT, it) }
	) {
		set(0, transform)
	}
}

/**
 * [Snapshot] accepting OpenGL meshes.
 */
class GLSnapshot : Snapshot {
	private val texture = GLTexture {
		PixelBuffer(RESOLUTION, RESOLUTION, 0, 4, MemoryUtil.memCalloc(RESOLUTION * RESOLUTION * 4), MemoryUtil::memFree)
	}
	private val frameBuffer = GLFrameBuffer(texture)

	override fun invoke(meshes: List<Mesh>): Texture {
		// clear any existing texture
		close()

		// like the above program, assume first attribute is position
		val positionBounds = meshes.map { it.bounds[0] }
		val minX = positionBounds.minOf {
			it[0].first
		}
		val maxX = positionBounds.maxOf {
			it[0].second
		}
		val minY = positionBounds.minOf {
			it[1].first
		}
		val maxY = positionBounds.maxOf {
			it[1].second
		}

		// scale the minimum amount to fit all dimensions to avoid stretching
		val scale = 2 / max(maxX - minX, maxY - minY)

		// scale to fit the viewport
		program[0] = transform.apply {
			reset()
			scale(Vector3.of(scale, scale))
		}

		frameBuffer.scoped {
			glViewport(0, 0, RESOLUTION, RESOLUTION)
			glClear(GL_COLOR_BUFFER_BIT)

			program.scoped {
				meshes.forEach { it.draw() }
			}
		}
		// regenerate mipmaps after updating texture
		glGenerateTextureMipmap(texture.id)

		return texture
	}

	override fun close() {
		texture.close()
		frameBuffer.close()
	}
}
