package dev.kkorolyov.pancake.core.io.component

import dev.kkorolyov.pancake.core.component.Force
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.WriteContext
import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Specification

import java.nio.ByteBuffer

class ForceSerializerSpec extends Specification {
	def buffer = ByteBuffer.allocate(128)
	def serializer = new ForceSerializer()

	def "serializes"() {
		def value = new Force().with {
			it.value.set(Vector3.of(x, y, z))
			it.offset.set(Vector3.of(y, z, x))
			it
		}

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
