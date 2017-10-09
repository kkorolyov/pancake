package dev.kkorolyov.pancake.platform.action

import dev.kkorolyov.pancake.platform.entity.Entity

import spock.lang.Specification

import static dev.kkorolyov.pancake.platform.action.MultiStageAction.ArmingOption.ACTIVATE
import static dev.kkorolyov.pancake.platform.action.MultiStageAction.ArmingOption.DEACTIVATE

class MultiStageActionSpec extends Specification {
	Entity entity = Mock()
	Action start = Mock()
	Action hold = Mock()
	Action end = Mock()

	MultiStageAction action = new MultiStageAction(start, hold, end)

	def "does nothing if not armed before applied"() {
		when:
		action.accept(entity)

		then:
		0 * _.accept(_)
	}

	def "{ACTIVATE} -> start"() {
		when:
		action.arm(ACTIVATE)
				.apply(entity)

		then:
		1 * start.accept(entity)
		0 * _.accept(_)
	}
	def "{DEACTIVATE} -> none"() {
		when:
		action.arm(DEACTIVATE)
				.apply(entity)

		then:
		0 * _.accept(_)
	}

	def "{ACTIVATE, ACTIVATE} -> start, hold"() {
		when:
		action.arm(ACTIVATE)
				.apply(entity)
		action.arm(ACTIVATE)
				.apply(entity)

		then:
		1 * start.accept(entity)
		1 * hold.accept(entity)
		0 * _.accept(_)
	}
	def "{ACTIVATE, DEACTIVATE} -> start, end"() {
		when:
		action.arm(ACTIVATE)
				.apply(entity)
		action.arm(DEACTIVATE)
				.apply(entity)

		then:
		1 * start.accept(entity)
		1 * end.accept(entity)
		0 * _.accept(_)
	}

	def "{ACTIVATE, ACTIVATE, ACTIVATE} -> start, hold, none"() {
		when:
		action.arm(ACTIVATE)
				.apply(entity)
		action.arm(ACTIVATE)
				.apply(entity)
		action.arm(ACTIVATE)
				.apply(entity)

		then:
		1 * start.accept(entity)
		1 * hold.accept(entity)
		0 * _.accept(_)
	}
	def "{ACTIVATE, ACTIVATE, DEACTIVATE} -> start, hold, end"() {
		when:
		action.arm(ACTIVATE)
				.apply(entity)
		action.arm(ACTIVATE)
				.apply(entity)
		action.arm(DEACTIVATE)
				.apply(entity)

		then:
		1 * start.accept(entity)
		1 * hold.accept(entity)
		1 * end.accept(entity)
	}
}
