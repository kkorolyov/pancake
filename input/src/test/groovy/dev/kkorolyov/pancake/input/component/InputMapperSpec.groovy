package dev.kkorolyov.pancake.input.component

import dev.kkorolyov.pancake.input.control.Control
import dev.kkorolyov.pancake.input.event.InputEvent
import dev.kkorolyov.pancake.platform.action.Action

import spock.lang.Specification

class InputMapperSpec extends Specification {
	InputMapper inputMapper = new InputMapper()

	def "returns first matched action"() {
		InputEvent event = Mock()
		Action action = Mock()

		Control control = Mock()
		Control control1 = Mock()
		Control control2 = Mock()

		[control, control1, control2].each(inputMapper::plusAssign)

		when:
		def result = inputMapper.invoke(event)

		then:
		1 * control.invoke(event)
		1 * control1.invoke(event) >> action
		0 * control2.invoke(_)

		result == action
	}
}
