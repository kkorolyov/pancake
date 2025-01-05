package dev.kkorolyov.pancake.input.control.io

import dev.kkorolyov.pancake.input.control.MouseButtonControl
import dev.kkorolyov.pancake.input.event.StateEvent
import dev.kkorolyov.pancake.platform.Registry
import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.WriteContext

import spock.lang.Specification

import java.nio.ByteBuffer

class MouseButtonControlSerializerSpec extends Specification {
	def buffer = ByteBuffer.allocate(64)
	def serializer = new MouseButtonControlSerializer()

	def "serializes"() {
		Action action = Mock()
		Registry.get(Action).put("someAction", action)
		def value = new MouseButtonControl(state.ordinal(), state, action)

		when:
		serializer.write(value, new WriteContext(buffer))

		then:
		serializer.read(new ReadContext(buffer.flip())) == value

		where:
		state << StateEvent.State.entries
	}
}
