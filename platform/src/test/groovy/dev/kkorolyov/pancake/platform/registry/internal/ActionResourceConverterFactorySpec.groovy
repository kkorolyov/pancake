package dev.kkorolyov.pancake.platform.registry.internal

import dev.kkorolyov.flub.function.convert.Converter
import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.action.CollectiveAction
import dev.kkorolyov.pancake.platform.registry.Registry
import dev.kkorolyov.pancake.platform.registry.Resource

import spock.lang.Specification

class ActionResourceConverterFactorySpec extends Specification {
	Action ref = Mock()
	Registry<Action> registry = new Registry<Action>()

	ActionResourceConverterFactory factory = new ActionResourceConverterFactory({ factory.get() })
	Converter<Object, Optional<Resource<Action>>> converter = factory.get()

	def setup() {
		registry.put("ref", Resource.constant(ref))
	}

	def "reads reference"() {
		expect:
		converter.convert("ref").orElse(null).get(registry) == ref
	}

	def "reads collective"() {
		expect:
		converter.convert(["ref", "ref"] as Object).orElse(null).get(registry) == new CollectiveAction(ref, ref)
	}
}
