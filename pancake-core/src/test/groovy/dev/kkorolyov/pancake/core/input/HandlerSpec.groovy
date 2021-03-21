package dev.kkorolyov.pancake.core.input

import dev.kkorolyov.pancake.platform.Resources
import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.action.MultiStageAction
import dev.kkorolyov.pancake.platform.application.Application
import dev.kkorolyov.pancake.platform.registry.Registry

import spock.lang.Shared
import spock.lang.Specification

import static dev.kkorolyov.pancake.core.input.HandlerSpec.InputCode.CODE
import static dev.kkorolyov.pancake.core.input.HandlerSpec.InputCode.MISSING
import static dev.kkorolyov.pancake.core.input.HandlerSpec.InputCode.OTHER
import static dev.kkorolyov.pancake.platform.action.MultiStageAction.ArmingOption.ACTIVATE
import static dev.kkorolyov.pancake.platform.action.MultiStageAction.ArmingOption.DEACTIVATE

class HandlerSpec extends Specification {
	@Shared
	long dt = 0
	@Shared
	Set<Enum> inputs = [CODE, OTHER]

	MultiStageAction delegate = Mock()

	Handler handler = new Handler(delegate, inputs)

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

	static class HandlerReaderSpec extends Specification {
		def "reads handlers from props"() {
			Application application = Mock() {
				toInput(_) >> { String key -> InputCode.valueOf(key) }
			}

			Action action = Mock()
			Registry<String, Action> registry = new Registry<>()
			registry.put("bogo", action)

			expect:
			new HandlerReader(application, registry).fromYaml(Resources.in("inputs.yaml").orElse(null)) == [new Handler(
					new MultiStageAction(action, null, null, 0),
					[CODE, OTHER]
			)] as Set
		}
	}

	enum InputCode {
		CODE,
		OTHER,
		MISSING
	}
}
