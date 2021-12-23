package dev.kkorolyov.pancake.platform.registry.internal

import dev.kkorolyov.flub.function.convert.Converter
import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.action.CollectiveAction
import dev.kkorolyov.pancake.platform.action.MultiStageAction
import dev.kkorolyov.pancake.platform.registry.Deferred
import dev.kkorolyov.pancake.platform.registry.Registry

import spock.lang.Specification

class ActionStratDeferredConverterFactorySpec extends Specification {
	Action ref = Mock()
	Registry<String, Action> registry = new Registry<>().with {
		put("ref", ref)
		it
	}
	ActionStratDeferredConverterFactory factory = new ActionStratDeferredConverterFactory({ factory.get() })
	Converter<Object, Optional<Deferred<String, Action>>> converter = factory.get()

	def "reads reference"() {
		expect:
		converter.convert("ref").orElse(null).resolve(registry.&get) == ref
	}

	def "reads collective"() {
		expect:
		converter.convert(["ref", "ref"] as Object).orElse(null).resolve(registry.&get) == new CollectiveAction([ref, ref])
	}

	def "reads multi-stage"() {
		expect:
		converter.convert([
				start: "ref",
				hold: ["ref", "ref"],
				end: "ref"
		]).orElse(null).resolve(registry.&get) == new MultiStageAction(
				ref,
				new CollectiveAction([ref, ref]),
				ref,
				ActionStratDeferredConverterFactory.MULTI_STAGE_HOLD_THRESHOLD
		)
	}
}
