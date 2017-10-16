package dev.kkorolyov.pancake.platform.action

import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.Signature

import spock.lang.Specification

class ActionSpec extends Specification {
	Entity entity = Mock()

	Action action = Spy()

	def "does nothing if entity signature does not contain action's signature"() {
		when:
		action.accept(entity)

		then:
		1 * entity.contains(_ as Signature) >> false
		0 * action.apply(_)
	}
	def "applies if entity signature contains action's signature"() {
		when:
		action.accept(entity)

		then:
		1 * entity.contains(_ as Signature) >> true
		1 * action.apply(entity) >> {}
	}
}
