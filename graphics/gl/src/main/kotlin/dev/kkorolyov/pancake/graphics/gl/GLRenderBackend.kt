package dev.kkorolyov.pancake.graphics.gl

import dev.kkorolyov.pancake.graphics.PixelBuffer
import dev.kkorolyov.pancake.graphics.RenderBackend
import dev.kkorolyov.pancake.graphics.resource.Buffer
import dev.kkorolyov.pancake.graphics.resource.Mesh
import dev.kkorolyov.pancake.graphics.resource.Program
import dev.kkorolyov.pancake.graphics.resource.Shader
import dev.kkorolyov.pancake.graphics.resource.Texture
import dev.kkorolyov.pancake.platform.math.Matrix4
import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.math.Vector3
import org.lwjgl.opengl.GL46.*
import org.lwjgl.system.MemoryStack
import java.util.IdentityHashMap
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set
import kotlin.math.floor
import kotlin.math.log2

class GLRenderBackend : RenderBackend {
	private val shaders = IdentityHashMap<Shader, VersionedId>()
	private val programs = IdentityHashMap<Program, VersionedId>()
	private val uniforms = IdentityHashMap<Program.Uniforms, Int>()
	private val textures = IdentityHashMap<Texture, VersionedId>()
	private val buffers = IdentityHashMap<Buffer<*>, VersionedId>()
	private val meshes = IdentityHashMap<Mesh, VersionedId>()

	private val textureFrameBuffer by lazy { glCreateFramebuffers() }

	override fun getShader(shader: Shader): Int {
		val result = shaders.getOrPut(shader) {
			VersionedId(createShader(shader), shader.version)
		}
		if (result.version != shader.version) {
			glDeleteShader(result.id)
			result.id = createShader(shader)
			result.version = shader.version
		}

		return result.id
	}

	private fun createShader(shader: Shader): Int {
		val id = glCreateShader(shader.type.value())
		if (id == 0) throw IllegalStateException("Cannot create shader")

		glShaderSource(id, shader.source)
		glCompileShader(id)

		if (glGetShaderi(id, GL_COMPILE_STATUS) == 0) throw IllegalArgumentException("Cannot compile ${shader.type} shader: ${glGetShaderInfoLog(id)}")

		return id
	}

	override fun getProgram(program: Program): Int {
		val result = programs.getOrPut(program) {
			val id = glCreateProgram()
			if (id == 0) throw IllegalStateException("Cannot create shader program")

			setProgram(id, program)

			VersionedId(id, program.version)
		}
		if (result.version != program.version) {
			setProgram(result.id, program)
			result.version = program.version
		}

		val uniformsVersion = uniforms.getOrPut(program.uniforms) {
			setUniforms(result.id, program.uniforms)
			program.uniforms.version
		}
		if (uniformsVersion != program.uniforms.version) {
			setUniforms(result.id, program.uniforms)
			uniforms[program.uniforms] = program.uniforms.version
		}

		return result.id
	}

	private fun setProgram(id: Int, program: Program) {
		program.shaders.forEach { glAttachShader(id, getShader(it)) }
		glLinkProgram(id)
		program.shaders.forEach { glDetachShader(id, getShader(it)) }

		if (glGetProgrami(id, GL_LINK_STATUS) == 0) throw IllegalArgumentException("Cannot link shader program: ${glGetProgramInfoLog(id)}")
	}

	private fun setUniforms(id: Int, uniforms: Program.Uniforms) {
		uniforms.forEach { (location, value) ->
			when (value) {
				is Float -> glProgramUniform1f(id, location, value)
				is Vector3 -> glProgramUniform3f(id, location, value.x.toFloat(), value.y.toFloat(), value.z.toFloat())
				is Vector2 -> glProgramUniform2f(id, location, value.x.toFloat(), value.y.toFloat())
				is Matrix4 -> MemoryStack.stackPush().use { stack ->
					glProgramUniformMatrix4fv(
						id,
						location,
						false,
						value.run {
							stack.floats(
								xx.toFloat(),
								yx.toFloat(),
								zx.toFloat(),
								wx.toFloat(),

								xy.toFloat(),
								yy.toFloat(),
								zy.toFloat(),
								wy.toFloat(),

								xz.toFloat(),
								yz.toFloat(),
								zz.toFloat(),
								wz.toFloat(),

								xw.toFloat(),
								yw.toFloat(),
								zw.toFloat(),
								ww.toFloat()
							)
						}
					)
				}

				else -> throw IllegalArgumentException("unknown uniform type: ${value::class}")
			}
		}
	}

	override fun getTexture(texture: Texture): Int {
		val result = textures.getOrPut(texture) {
			VersionedId(createTexture(texture), texture.version)
		}
		if (result.version != texture.version) {
			glDeleteTextures(result.id)
			result.id = createTexture(texture)
			result.version = texture.version
		}

		return result.id
	}

	private fun createTexture(texture: Texture): Int = texture.pixels().use { pixels ->
		val id = glCreateTextures(pixels.target())

		glTextureParameteri(id, GL_TEXTURE_WRAP_S, texture.wrapS.value())
		glTextureParameteri(id, GL_TEXTURE_WRAP_T, texture.wrapT.value())
		glTextureParameteri(id, GL_TEXTURE_MIN_FILTER, texture.filterMin.value())
		glTextureParameteri(id, GL_TEXTURE_MAG_FILTER, texture.filterMag.value())

		val (internalFormat, format) = pixels.format()
		val levels = 1 + floor(log2(maxOf(pixels.width, pixels.height, pixels.depth).toDouble())).toInt()
		when (pixels.target()) {
			GL_TEXTURE_1D -> {
				glTextureStorage1D(id, levels, internalFormat, pixels.width)
				glTextureSubImage1D(id, 0, 0, pixels.width, format, GL_UNSIGNED_BYTE, pixels.data)
			}

			GL_TEXTURE_2D -> {
				glTextureStorage2D(id, levels, internalFormat, pixels.width, pixels.height)
				glTextureSubImage2D(id, 0, 0, 0, pixels.width, pixels.height, format, GL_UNSIGNED_BYTE, pixels.data)
			}

			GL_TEXTURE_3D -> {
				glTextureStorage3D(id, levels, internalFormat, pixels.width, pixels.height, pixels.depth)
				glTextureSubImage3D(id, 0, 0, 0, 0, pixels.width, pixels.height, pixels.depth, format, GL_UNSIGNED_BYTE, pixels.data)
			}
		}

		// Generate mipmaps when requested by filtering
		if (levels > 1 && texture.filterMin.value() >= Texture.Filter.NEAREST_MIPMAP_NEAREST.value()) {
			glGenerateTextureMipmap(id)
		}

		id
	}

	override fun getBuffer(buffer: Buffer<*>): Int {
		val result = buffers.getOrPut(buffer) {
			val id = glCreateBuffers()
			glNamedBufferStorage(id, buffer.bytes.limit().toLong(), GL_MAP_WRITE_BIT)

			setBuffer(id, buffer)
			VersionedId(id, buffer.version)
		}
		if (result.version != buffer.version) {
			setBuffer(result.id, buffer)
			result.version = buffer.version
		}

		return result.id
	}

	private fun setBuffer(id: Int, buffer: Buffer<*>) {
		val bytes = buffer.bytes

		glMapNamedBufferRange(id, 0, buffer.bytes.limit().toLong(), GL_MAP_WRITE_BIT)!!.put(bytes)
		glUnmapNamedBuffer(id)
		bytes.rewind()
	}

	override fun getMesh(mesh: Mesh): Int {
		val result = meshes.getOrPut(mesh) {
			val id = glCreateVertexArrays()
			setMesh(id, mesh)
			VersionedId(id, mesh.version)
		}
		if (result.version != mesh.version) {
			setMesh(result.id, mesh)
			result.version = mesh.version
		} else {
			// ensure buffers are recent
			getBuffer(mesh.vertices)
			mesh.indices?.let(::getBuffer)
		}

		return result.id
	}

	private fun setMesh(id: Int, mesh: Mesh) {
		val structure = mesh.vertices.firstOrNull()?.map {
			when (it) {
				is Vector3 -> 3
				else -> 2
			}
		} ?: emptyList()

		// bind buffers
		glVertexArrayVertexBuffer(id, 0, getBuffer(mesh.vertices), 0, structure.sum() * Float.SIZE_BYTES)
		glVertexArrayElementBuffer(id, mesh.indices?.let(::getBuffer) ?: 0)

		// setup attribute layout
		var offset = 0
		structure.forEachIndexed { i, length ->
			glEnableVertexArrayAttrib(id, i)
			glVertexArrayAttribFormat(id, i, length, GL_FLOAT, false, offset * Float.SIZE_BYTES)
			glVertexArrayAttribBinding(id, i, 0)

			offset += length
		}
	}

	override fun viewport(x: Int, y: Int, width: Int, height: Int) {
		glViewport(x, y, width, height)
	}

	override fun clear() {
		glClear(GL_COLOR_BUFFER_BIT)
	}

	override fun draw(program: Program, meshes: Iterable<Mesh>) {
		glUseProgram(getProgram(program))

		meshes.forEach { mesh ->
			glBindVertexArray(getMesh(mesh))
			mesh.textures.forEachIndexed { i, texture ->
				glBindTextureUnit(i, getTexture(texture))
			}

			mesh.indices?.let { indices ->
				glDrawElements(mesh.mode.value(), indices.size, GL_UNSIGNED_INT, 0)
			} ?: glDrawArrays(mesh.mode.value(), 0, mesh.vertices.size)
		}

		glUseProgram(0)
	}

	override fun with(texture: Texture, op: () -> Unit) {
		val textureId = getTexture(texture)

		glNamedFramebufferTexture(textureFrameBuffer, GL_COLOR_ATTACHMENT0, textureId, 0)

		val status = glCheckNamedFramebufferStatus(textureFrameBuffer, GL_FRAMEBUFFER)
		if (status != GL_FRAMEBUFFER_COMPLETE) throw IllegalStateException("framebuffer [$textureFrameBuffer] incomplete - was ${Integer.toHexString(status)}")

		glBindFramebuffer(GL_FRAMEBUFFER, textureFrameBuffer)
		op()
		glGenerateTextureMipmap(textureId)
		glBindFramebuffer(GL_FRAMEBUFFER, 0)
	}
}

private data class VersionedId(var id: Int, var version: Int)

private fun Shader.Type.value() = when (this) {
	Shader.Type.VERTEX -> GL_VERTEX_SHADER
	Shader.Type.FRAGMENT -> GL_FRAGMENT_SHADER
}

private fun PixelBuffer.target() = if (depth > 0) GL_TEXTURE_3D else if (height > 0) GL_TEXTURE_2D else GL_TEXTURE_1D
private fun PixelBuffer.format() = if (channels == 4) GL_RGBA8 to GL_RGBA else if (channels == 3) GL_RGB8 to GL_RGB else if (channels == 2) GL_RG8 to GL_RG else GL_R8 to GL_RED

private fun Texture.Wrap.value() = when (this) {
	Texture.Wrap.REPEAT -> GL_REPEAT
	Texture.Wrap.MIRRORED_REPEAT -> GL_MIRRORED_REPEAT
	Texture.Wrap.CLAMP_TO_EDGE -> GL_CLAMP_TO_EDGE
	Texture.Wrap.CLAMP_TO_BORDER -> GL_CLAMP_TO_BORDER
}

private fun Texture.Filter.value() = when (this) {
	Texture.Filter.NEAREST -> GL_NEAREST
	Texture.Filter.LINEAR -> GL_LINEAR
	Texture.Filter.NEAREST_MIPMAP_NEAREST -> GL_NEAREST_MIPMAP_NEAREST
	Texture.Filter.LINEAR_MIPMAP_NEAREST -> GL_LINEAR_MIPMAP_NEAREST
	Texture.Filter.NEAREST_MIPMAP_LINEAR -> GL_NEAREST_MIPMAP_LINEAR
	Texture.Filter.LINEAR_MIPMAP_LINEAR -> GL_LINEAR_MIPMAP_LINEAR
}

private fun Mesh.Mode.value() = when (this) {
	Mesh.Mode.TRIANGLES -> GL_TRIANGLES
	Mesh.Mode.TRIANGLE_STRIP -> GL_TRIANGLE_STRIP
	Mesh.Mode.TRIANGLE_FAN -> GL_TRIANGLE_FAN
}
