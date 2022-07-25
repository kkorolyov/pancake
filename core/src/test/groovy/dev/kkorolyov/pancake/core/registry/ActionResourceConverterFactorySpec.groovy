package dev.kkorolyov.pancake.core.registry

import dev.kkorolyov.flub.function.convert.Converter
import dev.kkorolyov.pancake.core.action.ForceAction
import dev.kkorolyov.pancake.core.action.TransformAction
import dev.kkorolyov.pancake.core.action.VelocityAction
import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.math.Vector1
import dev.kkorolyov.pancake.platform.math.Vector3
import dev.kkorolyov.pancake.platform.registry.Resource

import spock.lang.Specification

class ActionResourceConverterFactorySpec extends Specification {
	ActionResourceConverterFactory factory = new ActionResourceConverterFactory()
	Converter<Object, Optional<Resource<Action>>> converter = factory.get()

	def "reads force"() {
		expect:
		converter.convert([
				force: [1, 2, 3]
		]).orElse(null).get(null) == new ForceAction(Vector3.of(1, 2, 3))
	}

	def "reads velocity"() {
		expect:
		converter.convert([
				velocity: [1, 2, 3]
		]).orElse(null).get(null) == new VelocityAction(Vector3.of(1, 2, 3))
	}

	def "reads transform"() {
		expect:
		converter.convert([
				position: [1, 2, 3]
		]).orElse(null).get(null) == new TransformAction(Vector3.of(1, 2, 3))
	}
	def "reads transform with orientation"() {
		expect:
		converter.convert([
				position: [1, 2, 3],
				orientation: [4]
		]).orElse(null).get(null) == new TransformAction(Vector3.of(1, 2, 3), Vector1.of(4))
	}
}
