package dev.kkorolyov.pancake.core.io.component

import dev.kkorolyov.pancake.core.component.limit.VelocityLimit
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.WriteContext

import spock.lang.Specification

import java.nio.ByteBuffer

class VelocityLimitSerializerSpec extends Specification {
	def buffer = ByteBuffer.allocate(128)
	def serializer = new VelocityLimitSerializer()

	def "serializes"() {
		def value = new VelocityLimit(limit, limit * 2)

		when:
		serializer.write(value, new WriteContext(buffer))

		then:
		serializer.read(new ReadContext(buffer.flip())) == value

		where:
		limit << (0..4)
	}
}
