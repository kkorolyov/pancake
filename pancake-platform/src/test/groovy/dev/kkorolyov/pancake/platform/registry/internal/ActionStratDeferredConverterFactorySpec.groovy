package dev.kkorolyov.pancake.platform.registry.internal

import dev.kkorolyov.flopple.function.convert.Converter
import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.action.CollectiveAction
import dev.kkorolyov.pancake.platform.action.MultiStageAction
import dev.kkorolyov.pancake.platform.registry.Registry

import spock.lang.Shared
import spock.lang.Specification

class ActionStratDeferredConverterFactorySpec extends Specification {
	@Shared
	String[] references = ["ref", "ref4", "newRef"]
	@Shared
	String[] multiStages = ["startStep", "holdStep", "endStep"]

	@Shared
	ActionStratDeferredConverterFactory factory = new ActionStratDeferredConverterFactory()

	Registry<String, Action> registry = new Registry<>();
	Converter<String, Optional<Action>> converter = factory.get(registry)

	def "reads reference"() {
		registry.put(name, action)

		expect:
		converter.convert(name).orElse(null) == action

		where:
		name << references
		action << references.collect { Mock(Action) }
	}

	def "reads collective"() {
		Map<String, Action> referenceToAction = references.collectEntries { [(it): Mock(Action)] }
		referenceToAction.each(registry.&put)

		expect:
		converter.convert(references as String).orElse(null) == new CollectiveAction(referenceToAction.values())
	}

	def "reads multi-stage"() {
		Map<String, Action> stageToAction = multiStages.collectEntries { [(it): Mock(Action)] }
		stageToAction.each(registry.&put)

		expect:
		converter.convert("{${multiStages.join(',')}}" as String).orElse(null) == new MultiStageAction(
				stageToAction.values()[0],
				stageToAction.values()[1],
				stageToAction.values()[2],
				ActionStratDeferredConverterFactory.MULTI_STAGE_HOLD_THRESHOLD
		)
	}
}
