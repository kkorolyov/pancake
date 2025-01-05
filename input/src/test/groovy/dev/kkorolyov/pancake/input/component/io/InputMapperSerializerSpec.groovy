package dev.kkorolyov.pancake.input.component.io

import dev.kkorolyov.pancake.input.component.InputMapper
import dev.kkorolyov.pancake.input.control.NoopControl
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.WriteContext

import spock.lang.Specification

import java.nio.ByteBuffer

class InputMapperSerializerSpec extends Specification {
	def buffer = ByteBuffer.allocate(256)
	def serializer = new InputMapperSerializer()

	def "serializes empty"() {
		def value = new InputMapper()

		when:
		serializer.write(value, new WriteContext(buffer))

		then:
		serializer.read(new ReadContext(buffer.flip())) == value
	}

	def "serializes"() {
		def value = new InputMapper().with { mapper ->
			(1..4).collect { NoopControl.INSTANCE }.each(mapper::plusAssign)
			mapper
		}

		when:
		serializer.write(value, new WriteContext(buffer))

		then:
		serializer.read(new ReadContext(buffer.flip())) == value
	}
}
