package dev.kkorolyov.pancake.graphics.resource.io

import dev.kkorolyov.pancake.graphics.PixelBuffer
import dev.kkorolyov.pancake.graphics.resource.Texture
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.Serializer
import dev.kkorolyov.pancake.platform.io.WriteContext

/**
 * Serializes [Texture]s.
 */
class TextureSerializer : Serializer<Texture> {
	override fun write(value: Texture, context: WriteContext) {
		context.putInt(value.wrapS.ordinal)
		context.putInt(value.wrapT.ordinal)
		context.putInt(value.filterMin.ordinal)
		context.putInt(value.filterMag.ordinal)
		value.pixels().use(context::putObject)
	}

	override fun read(context: ReadContext): Texture {
		val wrapS = Texture.Wrap.entries[context.int]
		val wrapT = Texture.Wrap.entries[context.int]
		val filterMin = Texture.Filter.entries[context.int]
		val filterMag = Texture.Filter.entries[context.int]
		val pixels = context.getObject(PixelBuffer::class.java)

		return Texture(
			wrapS,
			wrapT,
			filterMin,
			filterMag
		) { pixels }
	}

	override fun getType(): Class<Texture> = Texture::class.java
}
