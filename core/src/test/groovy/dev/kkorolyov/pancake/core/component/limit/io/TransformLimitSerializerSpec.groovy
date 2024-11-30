package dev.kkorolyov.pancake.core.component.limit.io

import dev.kkorolyov.pancake.core.component.limit.TransformLimit
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.WriteContext
import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Specification

import java.nio.ByteBuffer

class TransformLimitSerializerSpec extends Specification {
	def buffer = ByteBuffer.allocate(128)
	def serializer = new TransformLimitSerializer()

	def "serializes"() {
		def value = new TransformLimit()
		value.translationMin.set(Vector3.of(x, y, z))
		value.translationMax.set(Vector3.of(x + 2, y * 2, z + 3))
		value.scaleMin.set(Vector3.of(x, y, z))
		value.scaleMax.set(Vector3.of(x + 3, y * 3, z + 1))

		when:
		serializer.write(value, new WriteContext(buffer))

		then:
		serializer.read(new ReadContext(buffer.flip())) == value

		where:
		x << (0..4)
		y << (4..8)
		z << (0..8).step(2)
	}
}
