package dev.kkorolyov.pancake.core.component.io

import dev.kkorolyov.pancake.core.component.ActionQueue
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.WriteContext

import spock.lang.Specification

import java.nio.ByteBuffer

class ActionQueueSerializerSpec extends Specification {
	def buffer = ByteBuffer.allocate(1024)
	def serializer = new ActionQueueSerializer()

	def "serializes empty"() {
		def value = new ActionQueue()

		when:
		serializer.write(value, new WriteContext(buffer))

		then:
		serializer.read(new ReadContext(buffer.flip())) == value
	}
}
