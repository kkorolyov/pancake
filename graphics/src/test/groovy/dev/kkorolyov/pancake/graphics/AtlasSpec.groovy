package dev.kkorolyov.pancake.graphics

import spock.lang.Specification

import java.nio.ByteBuffer

class AtlasSpec extends Specification {
	static ByteBuffer byteBuffer(int ... bytes) {
		return ByteBuffer.wrap(bytes as byte[])
	}
	static byte[] array(ByteBuffer buffer) {
		byte[] result = new byte[buffer.capacity()]
		(0..<buffer.capacity()).each {
			result[it] = buffer.get(it)
		}
		return result
	}

	def "derives atlas size from inputs"() {
		when:
		Atlas atlas = new Atlas(*buffers)

		then:
		atlas.pixels.width == expectedWidth
		atlas.pixels.height == expectedWidth

		where:
		buffers << [
				[
						new PixelBuffer(1, 1, 0, 0, byteBuffer(), {}),
						new PixelBuffer(1, 1, 0, 0, byteBuffer(), {}),
						new PixelBuffer(1, 1, 0, 0, byteBuffer(), {}),
				],
				[
						new PixelBuffer(2, 3, 0, 0, byteBuffer(), {}),
				],
				[
						new PixelBuffer(2, 3, 0, 0, byteBuffer(), {}),
						new PixelBuffer(2, 2, 0, 0, byteBuffer(), {}),
						new PixelBuffer(2, 2, 0, 0, byteBuffer(), {}),
						new PixelBuffer(2, 2, 0, 0, byteBuffer(), {}),
				],
				[
						new PixelBuffer(3, 3, 0, 0, byteBuffer(), {}),
						new PixelBuffer(3, 3, 0, 0, byteBuffer(), {}),
						new PixelBuffer(3, 3, 0, 0, byteBuffer(), {}),
						new PixelBuffer(3, 3, 0, 0, byteBuffer(), {}),
				],
		]
		expectedWidth << [2, 4, 8, 8]
	}

	def "packs buffer data"() {
		when:
		Atlas atlas = new Atlas(*buffers)

		then:
		array(atlas.pixels.data) == expected as byte[]

		where:
		buffers << [
				[
						new PixelBuffer(1, 1, 0, 1, byteBuffer(1), {}),
						new PixelBuffer(1, 1, 0, 1, byteBuffer(1), {}),
				],
				[
						new PixelBuffer(1, 1, 0, 1, byteBuffer(1), {}),
						new PixelBuffer(1, 1, 0, 1, byteBuffer(1), {}),
						new PixelBuffer(1, 1, 0, 1, byteBuffer(1), {}),
				],
				[
						new PixelBuffer(1, 1, 0, 2, byteBuffer(1, 0), {}),
						new PixelBuffer(1, 1, 0, 2, byteBuffer(1, 1), {}),
						new PixelBuffer(1, 1, 0, 1, byteBuffer(1), {}),
				],
				[
						new PixelBuffer(2, 2, 0, 2, byteBuffer(1, 1, 1, 1), {}),
						new PixelBuffer(2, 2, 0, 2, byteBuffer(1, 1, 0, 1), {}),
						new PixelBuffer(2, 2, 0, 4, byteBuffer(1, 1, 0, 1, 1, 1, 1, 0), {}),
				],
		]
		expected << [
				[1, 1, 0, 0],
				[1, 1, 1, 0],
				[1, 0, 1, 1, 1, 0, 0, 0],
				[1, 1, 0, 0, 1, 1, 0, 0] + [0] * 1 * 4 + [1, 1, 0, 0, 0, 1, 0, 0] + [0] * 1 * 4 + [0] * 8 * 4 + [0] * 1 * 4 + [0] * 8 * 4 + [0] * 1 * 4 + [1, 1, 0, 1, 1, 1, 1, 0] + [0] * 38 * 4
		]
	}

	def "generates quads"() {
		when:
		Atlas atlas = new Atlas(*buffers)

		then:
		atlas.viewports == expected

		where:
		buffers << [
				[
						new PixelBuffer(1, 1, 0, 0, byteBuffer(), {}),
						new PixelBuffer(1, 1, 0, 0, byteBuffer(), {}),
						new PixelBuffer(1, 1, 0, 0, byteBuffer(), {}),
				],
				[
						new PixelBuffer(2, 3, 0, 0, byteBuffer(), {}),
				],
		]
		expected << [
				[
						new Atlas.Viewport(0, 0, 0.5, 0.5),
						new Atlas.Viewport(0.5, 0, 1, 0.5),
						new Atlas.Viewport(0, 0.5, 0.5, 1),
				],
				[
						new Atlas.Viewport(0, 0, 0.5, 0.75)
				],
		]
	}
}
