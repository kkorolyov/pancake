package dev.kkorolyov.pancake.platform.action

import spock.lang.Shared

import static dev.kkorolyov.pancake.platform.action.MultiStageAction.ArmingOption.ACTIVATE
import static dev.kkorolyov.pancake.platform.action.MultiStageAction.ArmingOption.DEACTIVATE

class MultiStageActionSpec {
	@Shared float holdThreshold = 0
	@Shared float dt = 0
	Action start = Mock()
	Action hold = Mock()
	Action end = Mock()

	Action action = new MultiStageAction(start, hold, end, holdThreshold)

	def "does nothing if not armed before applied"() {
		when:
		action.accept(id, entities)

		then:
		0 * _.accept(_)
	}

	def "avoids invoking null delegate actions"() {
		when:
		action = new MultiStageAction(null, null, null, holdThreshold)
		action.arm(ACTIVATE, dt)
				.apply(id, entities)
		action.arm(ACTIVATE, dt)
				.apply(id, entities)
		action.arm(DEACTIVATE, dt)
				.apply(id, entities)

		then:
		0 * _.accept(_)
	}

	def "{ACTIVATE} -> start"() {
		when:
		action.arm(ACTIVATE, dt)
				.apply(id, entities)

		then:
		1 * start.accept(id, entities)
		0 * _.accept(_)
	}
	def "{DEACTIVATE} -> none"() {
		when:
		action.arm(DEACTIVATE, dt)
				.apply(id, entities)

		then:
		0 * _.accept(_)
	}

	def "{ACTIVATE, ACTIVATE} -> start, hold"() {
		when:
		action.arm(ACTIVATE, dt)
				.apply(id, entities)
		action.arm(ACTIVATE, dt)
				.apply(id, entities)

		then:
		1 * start.accept(id, entities)
		1 * hold.accept(id, entities)
		0 * _.accept(_)
	}
	def "{ACTIVATE, DEACTIVATE} -> start, end"() {
		when:
		action.arm(ACTIVATE, dt)
				.apply(id, entities)
		action.arm(DEACTIVATE, dt)
				.apply(id, entities)

		then:
		1 * start.accept(id, entities)
		1 * end.accept(id, entities)
		0 * _.accept(_)
	}

	def "{ACTIVATE, ACTIVATE, ACTIVATE} -> start, hold, none"() {
		when:
		action.arm(ACTIVATE, dt)
				.apply(id, entities)
		action.arm(ACTIVATE, dt)
				.apply(id, entities)
		action.arm(ACTIVATE, dt)
				.apply(id, entities)

		then:
		1 * start.accept(id, entities)
		1 * hold.accept(id, entities)
		0 * _.accept(_)
	}
	def "{ACTIVATE, ACTIVATE, DEACTIVATE} -> start, hold, end"() {
		when:
		action.arm(ACTIVATE, dt)
				.apply(id, entities)
		action.arm(ACTIVATE, dt)
				.apply(id, entities)
		action.arm(DEACTIVATE, dt)
				.apply(id, entities)

		then:
		1 * start.accept(id, entities)
		1 * hold.accept(id, entities)
		1 * end.accept(id, entities)
	}

	def "{ACTIVATE, ACTIVATE(before holdThreshold)} -> start, none"() {
		float holdThreshold = 1
		float dt = holdThreshold - 0.1
		action = new MultiStageAction(start, hold, end, holdThreshold)

		when:
		action.arm(ACTIVATE, dt)
				.apply(id, entities)
		action.arm(ACTIVATE, dt)
				.apply(id, entities)

		then:
		1 * start.accept(id, entities)
		0 * _.accept(_)
	}
}
