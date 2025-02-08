package dev.kkorolyov.pancake.graphics

import dev.kkorolyov.pancake.graphics.resource.Buffer
import dev.kkorolyov.pancake.graphics.resource.Mesh
import dev.kkorolyov.pancake.graphics.resource.Texture
import dev.kkorolyov.pancake.graphics.resource.Vertex
import dev.kkorolyov.pancake.platform.math.Vector2
import org.lwjgl.stb.STBTTAlignedQuad
import org.lwjgl.stb.STBTTFontinfo
import org.lwjgl.stb.STBTTPackContext
import org.lwjgl.stb.STBTTPackRange
import org.lwjgl.stb.STBTTPackedchar
import org.lwjgl.stb.STBTruetype.stbtt_GetCodepointHMetrics
import org.lwjgl.stb.STBTruetype.stbtt_GetFontBoundingBox
import org.lwjgl.stb.STBTruetype.stbtt_GetFontVMetrics
import org.lwjgl.stb.STBTruetype.stbtt_GetPackedQuad
import org.lwjgl.stb.STBTruetype.stbtt_InitFont
import org.lwjgl.stb.STBTruetype.stbtt_PackBegin
import org.lwjgl.stb.STBTruetype.stbtt_PackEnd
import org.lwjgl.stb.STBTruetype.stbtt_PackFontRanges
import org.lwjgl.stb.STBTruetype.stbtt_ScaleForPixelHeight
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import java.io.InputStream
import kotlin.math.max
import kotlin.math.min

private const val pw = 512
private const val ph = 512

private const val firstChar = 32
private const val numChars = 96

private const val indent = 2

/**
 * Generates meshes for rendering text using the font from `inStream` at pixel height `size`.
 * Mesh vertices take the form `(vec2 pos, vec2 texCoord)`.
 * Shaders drawing these meshes should use the sampled texture's `r` component as an alpha value.
 * For proper blending, ensure that `glEnable(GL_BLEND)` and `glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)` are set.
 */
class Font(inStream: InputStream, size: Int) : AutoCloseable {
	private val fData = inStream.readBytes().let { bytes ->
		val buffer = MemoryUtil.memAlloc(bytes.size)
		bytes.forEach(buffer::put)
		buffer.flip()
	}
	private val cdata = STBTTPackedchar.malloc(numChars)
	private val pixels = MemoryUtil.memAlloc(pw * ph).apply {
		MemoryStack.stackPush().use { stack ->
			val ranges = STBTTPackRange.malloc(1, stack)
			ranges.put(0, STBTTPackRange.malloc(stack).set(size.toFloat(), firstChar, null, cdata.capacity(), cdata, 0, 0))

			STBTTPackContext.malloc(stack).let {
				if (!stbtt_PackBegin(it, this, pw, ph, 0, 1)) {
					MemoryUtil.memFree(fData)
					cdata.close()
					MemoryUtil.memFree(this)

					throw IllegalStateException("cannot pack font")
				}
				if (!stbtt_PackFontRanges(it, fData, 0, ranges)) {
					MemoryUtil.memFree(fData)
					cdata.close()
					MemoryUtil.memFree(this)

					throw IllegalStateException("cannot pack font")
				}
				stbtt_PackEnd(it)
			}
		}
	}
	private val bitmap = Texture {
		PixelBuffer(pw, ph, 0, 1, pixels) {}
	}

	private val scale: Float
	private val lineHeight: Float
	private val charWidth: Float
	private val indentWidth: Float

	init {
		STBTTFontinfo.malloc().apply {
			val loaded = stbtt_InitFont(this, fData)
			if (!loaded) {
				MemoryUtil.memFree(fData)
				cdata.close()
				MemoryUtil.memFree(pixels)
				close()

				throw IllegalStateException("cannot initialize font")
			}
		}.use { info ->
			scale = stbtt_ScaleForPixelHeight(info, size.toFloat())

			MemoryStack.stackPush().use { stack ->
				val ascentP = stack.mallocInt(1)
				val descentP = stack.mallocInt(1)
				val lineGapP = stack.mallocInt(1)
				stbtt_GetFontVMetrics(info, ascentP, descentP, lineGapP)
				lineHeight = (ascentP[0] - descentP[0] + lineGapP[0]) * scale

				val charWidthP = stack.mallocInt(1)
				stbtt_GetFontBoundingBox(info, stack.mallocInt(1), stack.mallocInt(1), charWidthP, stack.mallocInt(1))
				charWidth = charWidthP[0] * scale

				val spaceWidthP = stack.mallocInt(1)
				stbtt_GetCodepointHMetrics(info, ' '.code, spaceWidthP, stack.mallocInt(1))
				indentWidth = spaceWidthP[0] * scale * indent
			}
		}
	}

	/**
	 * Returns a mesh for drawing [text].
	 */
	operator fun invoke(text: String): Mesh {
		var minX = 0.0
		var minY = 0.0
		var maxX = 0.0
		var maxY = 0.0

		val vertices = mutableListOf<Vertex>().apply {
			MemoryStack.stackPush().use { stack ->
				val xP = stack.floats(0f)
				val yP = stack.floats(0f)
				val quad = STBTTAlignedQuad.malloc(stack)

				text.toCharArray().forEach { c ->
					if (c == '\n') {
						// line break
						xP.put(0, 0f)
						yP.put(0, yP[0] + lineHeight)
					} else if (c == '\t') {
						// indent
						xP.put(0, xP[0] + indentWidth)
					} else {
						val cI = c.code - firstChar
						if (cI < 0 || cI > numChars) {
							// unknown - display blank
							xP.put(0, xP[0] + charWidth)
						} else {
							stbtt_GetPackedQuad(cdata, pw, ph, cI, xP, yP, quad, true)

							val x0 = (quad.x0() * scale).toDouble()
							val x1 = (quad.x1() * scale).toDouble()
							val y0 = -(quad.y0() * scale).toDouble()
							val y1 = -(quad.y1() * scale).toDouble()

							val s0 = quad.s0().toDouble()
							val s1 = quad.s1().toDouble()
							val t0 = quad.t0().toDouble()
							val t1 = quad.t1().toDouble()

							add(Vertex(Vector2.of(x0, y0), Vector2.of(s0, t0)))
							add(Vertex(Vector2.of(x1, y0), Vector2.of(s1, t0)))
							add(Vertex(Vector2.of(x1, y1), Vector2.of(s1, t1)))
							add(Vertex(Vector2.of(x0, y1), Vector2.of(s0, t1)))

							minX = min(minX, x0)
							minY = max(minY, y0)
							maxX = max(maxX, x1)
							maxY = min(maxY, y1)
						}
					}
				}
			}
		}.toTypedArray()

		// center around (0,0)
		val translate = Vector2.of((minX - maxX) / 2, -(minY + maxY) / 2)
		vertices.forEachIndexed { i, vertex ->
			vertices[i] = Vertex(Vector2.of(vertex[0]).apply { add(translate) }, vertex[1])
		}

		return Mesh(
			Buffer.vertex(*vertices),
			Buffer.int(
				*text.indices.flatMap { i ->
					sequenceOf(
						i * 4,
						i * 4 + 1,
						i * 4 + 3,
						i * 4 + 3,
						i * 4 + 1,
						i * 4 + 2
					)
				}.toIntArray()
			),
			textures = listOf(bitmap)
		)
	}

	/**
	 * Frees all memory allocated by this font.
	 */
	override fun close() {
		MemoryUtil.memFree(fData)
		cdata.close()
		MemoryUtil.memFree(pixels)
	}
}
