package dev.kkorolyov.pancake.core.component.io

import dev.kkorolyov.pancake.core.component.Damping
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.WriteContext
import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Specification

import java.nio.ByteBuffer

class DampingSerializerSpec extends Specification {
	def buffer = ByteBuffer.allocate(128)
	def serializer = new DampingSerializer()

	def "serializes"() {
		def value = new Damping(Vector3.of(0, 0.1, 0.5), Vector3.of(0.5, 0.7, 0.8))

		when:
		serializer.write(value, new WriteContext(buffer))

		then:
		serializer.read(new ReadContext(buffer.flip())) == value
	}
}
