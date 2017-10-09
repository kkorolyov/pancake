package dev.kkorolyov.pancake.platform.action

import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.Signature

import spock.lang.Shared
import spock.lang.Specification

class ActionSpec extends Specification {
	@Shared Signature signature = new Signature()
	Entity entity = Mock()

	Action action = new TestAction(signature)

	def "does nothing if entity signature does not contain action's signature"() {
		when:
		action.accept(entity)

		then:
		1 * entity.contains(signature) >> false
		0 * entity.toString()
	}
	def "applies if entity signature contains action's signature"() {
		when:
		action.accept(entity)

		then:
		1 * entity.contains(signature) >> true
		1 * entity.toString()
	}

	private class TestAction extends Action {
		protected TestAction(Signature signature) {
			super(signature)
		}

		@Override
		protected void apply(Entity entity) {
			entity.toString();
		}
	}
}
