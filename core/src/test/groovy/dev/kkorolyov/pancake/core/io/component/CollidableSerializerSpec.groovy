package dev.kkorolyov.pancake.core.io.component

import dev.kkorolyov.pancake.core.component.tag.Collidable
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.WriteContext

import spock.lang.Specification

import java.nio.ByteBuffer

class CollidableSerializerSpec extends Specification {
	def buffer = ByteBuffer.allocate(128)
	def serializer = new CollidableSerializer()

	def "serializes"() {
		def value = new Collidable(priority)

		when:
		serializer.write(value, new WriteContext(buffer))

		then:
		serializer.read(new ReadContext(buffer.flip())) == value

		where:
		priority << (-4..4)
	}
}
