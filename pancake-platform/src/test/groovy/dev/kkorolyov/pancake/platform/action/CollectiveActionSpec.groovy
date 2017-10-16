package dev.kkorolyov.pancake.platform.action

import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.Signature

import spock.lang.Specification

class CollectiveActionSpec extends Specification {
	Entity entity = Mock()
	List<Action> subActions = (1..4).collect {Mock(Action)}

	CollectiveAction action = new CollectiveAction(subActions)

	def "applies all subactions"() {
		when:
		action.accept(entity)

		then:
		1 * entity.contains(new Signature()) >> accept
		subActions.each {it.accept(entity)}

		where:
		accept << [true, false]
	}
}
