package dev.kkorolyov.pancake.platform.action

import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.EntityPool

import spock.lang.Shared
import spock.lang.Specification

class CollectiveActionSpec extends Specification {
	@Shared Entity entity = new EntityPool().create()

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
