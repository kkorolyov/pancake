package dev.kkorolyov.pancake.graphics.component.io

import dev.kkorolyov.pancake.graphics.component.Lens
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.WriteContext
import dev.kkorolyov.pancake.platform.math.Vector2

import spock.lang.Specification

import java.nio.ByteBuffer

class LensSerializerSpec extends Specification {
	def buffer = ByteBuffer.allocate(128)
	def serializer = new LensSerializer()

	def "serializes empty mask"() {
		def value = new Lens(
				Vector2.of(x, y),
				Vector2.of(y, x),
				Vector2.of(-x, y),
				BitSet.valueOf(),
				active
		)

		when:
		serializer.write(value, new WriteContext(buffer))

		then:
		serializer.read(new ReadContext(buffer.flip())) == value

		where:
		x << (1..4)
		y << (4..7)
		active << (1..4).collect { it % 2 == 0 }
	}

	def "serializes"() {
		def value = new Lens(
				Vector2.of(x, y),
				Vector2.of(y, x),
				Vector2.of(-x, y),
				BitSet.valueOf([x, y, -x, -y].toArray() as byte[]),
				active
		)

		when:
		serializer.write(value, new WriteContext(buffer))

		then:
		serializer.read(new ReadContext(buffer.flip())) == value

		where:
		x << (1..4)
		y << (4..7)
		active << (1..4).collect { it % 2 == 0 }
	}
}
