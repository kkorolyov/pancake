package dev.kkorolyov.pancake.platform.action

class CollectiveActionSpec extends ActionSpec {
	List<Action> subActions = (1..4).collect {Mock(Action)}

	@Override
	CollectiveAction initAction() {
		return new CollectiveAction(subActions)
	}

	def "applies all subactions if accepted"() {
		when:
		action.accept(id, entities)

		then:
		1 * entities.contains(id, signature) >> true
		subActions.each {
			1 * it.accept(id, entities)
		}
	}
	def "applies no subactions if not accepted"() {
		when:
		action.accept(id, entities)

		then:
		1 * entities.contains(id, signature) >> false
		subActions.each {
			0 * it.accept(_, _)
		}
	}
}
