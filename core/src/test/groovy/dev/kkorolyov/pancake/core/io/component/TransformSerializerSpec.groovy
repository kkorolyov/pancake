package dev.kkorolyov.pancake.core.io.component

import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.WriteContext
import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Specification

import java.nio.ByteBuffer

class TransformSerializerSpec extends Specification {
	def buffer = ByteBuffer.allocate(512)
	def serializer = new TransformSerializer()

	def "serializes"() {
		def value = new Transform().with {
			it.translation.set(Vector3.of(x, y, z))
			it.scale.set(Vector3.of(z, y, x))
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

	def "serializes with parent"() {
		def value = new Transform().with {
			it.translation.set(Vector3.of(x, y, z))
			it.scale.set(Vector3.of(z, y, x))
			it.parent = new Transform().with {
				it.translation.set(Vector3.of(z, y, x))
				it.scale.set(Vector3.of(x, y, z))
				it
			}
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
