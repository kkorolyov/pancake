package dev.kkorolyov.pancake.core.component.io

import dev.kkorolyov.pancake.core.component.Mass
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.WriteContext

import spock.lang.Specification

import java.nio.ByteBuffer

class MassSerializerSpec extends Specification {
	def buffer = ByteBuffer.allocate(128)
	def serializer = new MassSerializer()

	def "serializes"() {
		def value = new Mass(massValue)

		when:
		serializer.write(value, new WriteContext(buffer))

		then:
		serializer.read(new ReadContext(buffer.flip())) == value

		where:
		massValue << (-4..4)
	}
}
