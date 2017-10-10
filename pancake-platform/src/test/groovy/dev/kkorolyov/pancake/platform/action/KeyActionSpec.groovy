package dev.kkorolyov.pancake.platform.action

import dev.kkorolyov.pancake.platform.entity.Entity
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton

import spock.lang.Shared
import spock.lang.Specification

import static dev.kkorolyov.pancake.platform.SpecUtilities.randKeyCode
import static dev.kkorolyov.pancake.platform.SpecUtilities.randMouseButton

class KeyActionSpec extends Specification {
	@Shared float holdThreshold = 0
	@Shared float dt = 0
	@Shared Set<Enum> inputs = [KeyCode.A, MouseButton.PRIMARY]
	Entity entity = Mock()
	Action start = Mock()
	Action hold = Mock()
	Action end = Mock()

	KeyAction action = new KeyAction(start, hold, end, holdThreshold, inputs)

	def "accepts inputs of valid types"() {
		when:
		new KeyAction(start, hold, end, holdThreshold, values)

		then:
		notThrown IllegalArgumentException

		where:
		values << [
				randKeyCode(),
				randMouseButton(),
				[randKeyCode(), randMouseButton()]
		]
	}
	def "declines inputs of invalid types"() {
		when:
		new KeyAction(start, hold, end, holdThreshold, values)

		then:
		thrown IllegalArgumentException

		where:
		values << [
				MultiStageAction.ArmingOption.ACTIVATE,
				[randKeyCode(), MultiStageAction.ArmingOption.DEACTIVATE],
				[randMouseButton(), MultiStageAction.ArmingOption.ACTIVATE],
				[randKeyCode(), randMouseButton(), MultiStageAction.ArmingOption.ACTIVATE]
		]
	}

	def "inclusive superset of inputs counts as ACTIVE"() {
		when:
		action.arm(values, dt)
				.apply(entity)

		then:
		1 * start.accept(entity)
		0 * _.accept(_)

		where:
		values << [inputs, inputs << KeyCode.B]
	}
	def "exclusive subset of inputs counts as INACTIVE"() {
		when:
		action.arm(values, dt)
				.apply(entity)

		then:
		0 * _.accept(_)

		where:
		values << [[].toSet(), inputs.collate(2)[0].toSet(), inputs.collate(2)[1].toSet()]
	}
}
