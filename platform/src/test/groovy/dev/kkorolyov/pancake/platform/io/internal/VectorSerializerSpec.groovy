package dev.kkorolyov.pancake.platform.io.internal

import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.WriteContext
import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Specification

import java.nio.ByteBuffer

class VectorSerializerSpec extends Specification {
	def buffer = ByteBuffer.allocate(256)
	def serializer = new VectorSerializer()

	def "serializes vector2"() {
		Vector2 value = Vector2.of(x, y)

		when:
		serializer.write(value, new WriteContext(buffer))

		then:
		serializer.read(new ReadContext(buffer.flip())) == value

		where:
		x << (-4..4)
		y << (-4..4).step(-1)
	}

	def "serializes vector3"() {
		Vector3 value = Vector3.of(x, y, z)

		when:
		serializer.write(value, new WriteContext(buffer))

		then:
		serializer.read(new ReadContext(buffer.flip())) == value

		where:
		x << (-4..4)
		y << (-4..4).step(-1)
		z << (-8..8).by(2)
	}
}
