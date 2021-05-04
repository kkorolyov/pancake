package dev.kkorolyov.pancake.platform.action

import dev.kkorolyov.pancake.platform.entity.Entity

import spock.lang.Shared
import spock.lang.Specification

class CollectiveActionStratSpec extends Specification {
	@Shared Entity entity = Mock()

	List<Action> subActions = (1..4).collect { Mock(Action) }

	Action action = new CollectiveAction(subActions)

	def "applies all subactions"() {
		when:
		action.apply(entity)

		then:
		subActions.each {
			1 * it.apply(entity)
		}
	}
}
