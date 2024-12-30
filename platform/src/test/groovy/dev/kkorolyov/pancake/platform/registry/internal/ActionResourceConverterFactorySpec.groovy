package dev.kkorolyov.pancake.platform.registry.internal

import dev.kkorolyov.flub.function.convert.Converter
import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.Registry
import dev.kkorolyov.pancake.platform.registry.Resource

import spock.lang.Specification

class ActionResourceConverterFactorySpec extends Specification {
	Action ref = Mock()
	Registry<Action> registry = Registry.get(Action)

	ActionResourceConverterFactory factory = new ActionResourceConverterFactory({ Converter.enforcing(factory.get()) })
	Converter<Object, Resource<Action>> converter = Converter.enforcing(factory.get())

	def setup() {
		registry.put("ref", ref)
	}

	def "reads reference"() {
		expect:
		converter.convert("ref").get(registry) == ref
	}

	def "reads many"() {
		Entity entity = new EntityPool().create()

		when:
		Action result = converter.convert(["ref", "ref"] as Object).get(registry)
		result.apply(entity)

		then:
		2 * ref.apply(entity)
	}
}
