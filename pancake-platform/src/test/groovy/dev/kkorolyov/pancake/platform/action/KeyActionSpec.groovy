package dev.kkorolyov.pancake.platform.action

import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton

import spock.lang.Shared

import static dev.kkorolyov.pancake.platform.SpecUtilities.randKeyCode
import static dev.kkorolyov.pancake.platform.SpecUtilities.randMouseButton
import static dev.kkorolyov.pancake.platform.action.MultiStageAction.ArmingOption.ACTIVATE
import static dev.kkorolyov.pancake.platform.action.MultiStageAction.ArmingOption.DEACTIVATE

class KeyActionSpec {
	@Shared float dt = 0
	@Shared Set<Enum> inputs = [KeyCode.A, MouseButton.PRIMARY]
	MultiStageAction delegate = Mock()

	Action action = new KeyAction(delegate, inputs)

	def "accepts inputs of valid types"() {
		when:
		new KeyAction(delegate, values)

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
		new KeyAction(delegate, values)

		then:
		thrown IllegalArgumentException

		where:
		values << [
				ACTIVATE,
				[randKeyCode(), DEACTIVATE],
				[randMouseButton(), ACTIVATE],
				[randKeyCode(), randMouseButton(), ACTIVATE]
		]
	}

	def "inclusive superset of inputs translates to ACTIVATE"() {
		when:
		action.arm(values, dt)
				.apply(id, entities)

		then:
		1 * delegate.arm(ACTIVATE, dt)

		where:
		values << [inputs, inputs << KeyCode.B]
	}
	def "exclusive subset of inputs translates to DEACTIVATE"() {
		when:
		action.arm(values, dt)
				.apply(id, entities)

		then:
		1 * delegate.arm(DEACTIVATE, dt)

		where:
		values << [[].toSet(), inputs.collate(2)[0].toSet(), inputs.collate(2)[1].toSet()]
	}
}
