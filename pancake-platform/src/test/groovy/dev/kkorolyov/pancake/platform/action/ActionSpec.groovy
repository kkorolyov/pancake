package dev.kkorolyov.pancake.platform.action

import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.entity.Signature
import org.spockframework.mock.MockUtil

import spock.lang.Shared
import spock.lang.Specification

import static dev.kkorolyov.pancake.platform.SpecUtilities.setField

class ActionSpec extends Specification {
	@Shared MockUtil detector = new MockUtil()

	UUID id = UUID.randomUUID()
	EntityPool entities = Mock()
	Signature signature = Mock()

	Action action

	def setup() {
		action = initAction()
		setField("signature", Action, action, signature)
	}
	Action initAction() {
		return Spy(Action)
	}

	def "does nothing if entity signature does not contain action's signature"() {
		when:
		action.accept(id, entities)

		then:
		1 * entities.contains(id, signature) >> false
		if (detector.isMock(action)) 0 * action.apply(_, _)
	}
	def "applies if entity signature contains action's signature"() {
		when:
		action.accept(id, entities)

		then:
		1 * entities.contains(id, signature) >> true
		if (detector.isMock(action)) 1 * action.apply(id, entities) >> {}
	}
}
