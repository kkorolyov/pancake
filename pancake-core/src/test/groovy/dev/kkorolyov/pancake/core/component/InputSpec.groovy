package dev.kkorolyov.pancake.core.component

import dev.kkorolyov.pancake.platform.Resources
import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.action.MultiStageAction
import dev.kkorolyov.pancake.platform.application.Application
import dev.kkorolyov.pancake.platform.registry.Registry
import dev.kkorolyov.simpleprops.Properties

import spock.lang.Shared
import spock.lang.Specification

import static dev.kkorolyov.pancake.core.component.InputSpec.HandlerSpec.InputCode.CODE
import static dev.kkorolyov.pancake.core.component.InputSpec.HandlerSpec.InputCode.MISSING
import static dev.kkorolyov.pancake.core.component.InputSpec.HandlerSpec.InputCode.OTHER
import static dev.kkorolyov.pancake.platform.action.MultiStageAction.ArmingOption.ACTIVATE
import static dev.kkorolyov.pancake.platform.action.MultiStageAction.ArmingOption.DEACTIVATE
import static dev.kkorolyov.simplespecs.SpecUtilities.randString
import static dev.kkorolyov.simplespecs.SpecUtilities.setField

class InputSpec extends Specification {
	static class HandlerSpec extends Specification {
		@Shared
		long dt = 0
		@Shared
		Set<Enum> inputs = [CODE, OTHER]

		MultiStageAction delegate = Mock()

		Input.Handler handler = new Input.Handler(delegate, inputs)

		def setupSpec() {
			setField("APPLICATION", Resources, Mock(Application) {
				toInput(CODE.name()) >> CODE
				toInput(OTHER.name()) >> OTHER
			})
		}

		def "inclusive superset of inputs translates to ACTIVATE"() {
			when:
			handler.arm(values, dt)

			then:
			1 * delegate.arm(ACTIVATE, dt)

			where:
			values << [inputs, inputs << MISSING]
		}
		def "exclusive subset of inputs translates to DEACTIVATE"() {
			when:
			handler.arm(values, dt)

			then:
			1 * delegate.arm(DEACTIVATE, dt)

			where:
			values << [[].toSet(), inputs.collate(2)[0].toSet(), inputs.collate(2)[1].toSet()]
		}

		def "reads handlers from props"() {
			String name = randString()
			Action action = Mock()

			Properties props = new Properties()
			Registry<String, Action> registry = new Registry<>()

			props.put(name, "($CODE, $OTHER)")
			registry.put(name, action)

			expect:
			Input.Handler.fromProperties(props, registry) == [new Input.Handler(
					new MultiStageAction(action, null, null, 0),
					[CODE, OTHER]
			)] as Set
		}

		enum InputCode {
			CODE,
			OTHER,
			MISSING
		}
	}
}
