package dev.kkorolyov.pancake.core.registry

import dev.kkorolyov.flopple.function.convert.Converter
import dev.kkorolyov.pancake.core.action.ForceAction
import dev.kkorolyov.pancake.core.action.TransformAction
import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.math.Vectors
import dev.kkorolyov.pancake.platform.registry.Deferred
import dev.kkorolyov.pancake.platform.registry.internal.VectorStratDeferredConverterFactory

import spock.lang.Specification

class ActionStratDeferredConverterFactorySpec extends Specification {
	ActionStratDeferredConverterFactory factory = new ActionStratDeferredConverterFactory({ new VectorStratDeferredConverterFactory().get() })
	Converter<Object, Optional<Deferred<String, Action>>> converter = factory.get()

	def "reads force"() {
		expect:
		converter.convert([
				force: [1, 2, 3]
		]).orElse(null).resolve() == new ForceAction(Vectors.create(1, 2, 3))
	}

	def "reads transform"() {
		expect:
		converter.convert([
				position: [1, 4, 2],
		]).orElse(null).resolve() == new TransformAction(Vectors.create(1, 4, 2))
	}
	def "reads transform with rotation"() {
		expect:
		converter.convert([
				position: [1, 4, 2],
				orientation: [2]
		]).orElse(null).resolve() == new TransformAction(Vectors.create(1, 4, 2), Vectors.create(2))
	}
}
