package dev.kkorolyov.pancake.core.registry

import dev.kkorolyov.flub.function.convert.Converter
import dev.kkorolyov.pancake.core.action.ForceAction
import dev.kkorolyov.pancake.core.action.PositionAction
import dev.kkorolyov.pancake.core.action.VelocityAction
import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.math.Vector3
import dev.kkorolyov.pancake.platform.registry.Resource

import spock.lang.Specification

class ActionResourceConverterFactorySpec extends Specification {
	ActionResourceConverterFactory factory = new ActionResourceConverterFactory()
	Converter<Object, Resource<Action>> converter = Converter.enforcing(factory.get())

	def "reads force"() {
		expect:
		converter.convert([
				force: [1, 2, 3]
		]).get(null) == new ForceAction(Vector3.of(1, 2, 3))
	}

	def "reads velocity"() {
		expect:
		converter.convert([
				velocity: [1, 2, 3]
		]).get(null) == new VelocityAction(Vector3.of(1, 2, 3))
	}

	def "reads position"() {
		expect:
		converter.convert([
				position: [1, 2, 3]
		]).get(null) == new PositionAction(Vector3.of(1, 2, 3))
	}
}
