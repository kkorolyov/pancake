package dev.kkorolyov.pancake.core.animation.io

import dev.kkorolyov.pancake.core.animation.TransformFrame
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.WriteContext
import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Specification

import java.nio.ByteBuffer

class TransformFrameSerializerSpec extends Specification {
	def buffer = ByteBuffer.allocate(256)
	def serializer = new TransformFrameSerializer()

	def "serializes"() {
		def value = new TransformFrame().with {
			it.translation.set(Vector3.of(x, y, z))
			it.rotation.set(Vector3.of(z, y, x))
			it.scale.set(Vector3.of(x, z, y))
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
