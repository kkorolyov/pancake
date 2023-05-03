package dev.kkorolyov.pancake.graphics.gl.resource

import dev.kkorolyov.pancake.graphics.resource.FrameBuffer
import dev.kkorolyov.pancake.graphics.resource.Texture
import dev.kkorolyov.pancake.graphics.util.Cache
import org.lwjgl.opengl.GL46.*

/**
 * An `OpenGL` frame buffer that can be reused across shared contexts.
 * Draws color data to [texture].
 */
class GLFrameBuffer(texture: Texture) : FrameBuffer {
	private val cache = Cache {
		val id = glCreateFramebuffers()
		glNamedFramebufferTexture(id, GL_COLOR_ATTACHMENT0, texture.id, 0)

		val status = glCheckNamedFramebufferStatus(id, GL_FRAMEBUFFER)
		if (status != GL_FRAMEBUFFER_COMPLETE) throw IllegalStateException("framebuffer [$id] incomplete - was ${Integer.toHexString(status)}")

		id
	}

	override val id by cache

	override fun activate() {
		glBindFramebuffer(GL_FRAMEBUFFER, id)
	}

	override fun deactivate() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0)
	}

	override fun close() {
		cache.invalidate(::glDeleteFramebuffers)
	}
}
