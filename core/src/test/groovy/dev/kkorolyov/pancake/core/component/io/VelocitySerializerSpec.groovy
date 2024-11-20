package dev.kkorolyov.pancake.core.component.io

import dev.kkorolyov.pancake.core.component.Velocity
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.WriteContext
import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Specification

import java.nio.ByteBuffer

class VelocitySerializerSpec extends Specification {
	def buffer = ByteBuffer.allocate(128)
	def serializer = new VelocitySerializer()

	def "serializes"() {
		def value = new Velocity().with {
			it.linear.set(Vector3.of(x, y, z))
			it.angular.set(Vector3.of(y, z, x))
			it
		}

		when:
		serializer.write(value, new WriteContext(buffer))

		then:
		serializer.read(new ReadContext(buffer.flip())) == value

		where:
		x << (-4..4)
		y << (-4..4).step(-1)
		z << (-8..8).step(2)
	}
}
