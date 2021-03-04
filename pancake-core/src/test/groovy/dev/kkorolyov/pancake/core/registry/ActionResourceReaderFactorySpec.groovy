package dev.kkorolyov.pancake.core.registry

import dev.kkorolyov.flopple.function.convert.Converter
import dev.kkorolyov.pancake.core.action.ForceAction
import dev.kkorolyov.pancake.core.action.TransformAction
import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.math.Vector
import dev.kkorolyov.pancake.platform.registry.Registry

import spock.lang.Shared
import spock.lang.Specification

import static dev.kkorolyov.pancake.platform.SpecUtilities.randVector

class ActionResourceReaderFactorySpec extends Specification {
	@Shared
	Vector vector = randVector()
	@Shared
	Vector vector1 = randVector()

	@Shared
	ActionResourceReaderFactory factory = new ActionResourceReaderFactory()

	Registry<String, Action> registry = new Registry<>()
	Converter<String, Optional<Action>> converter = factory.get(registry)

	def "reads force"() {
		ForceAction action = new ForceAction(vector)

		expect:
		converter.convert("FORCE($vector.x, $vector.y, $vector.z)" as String).orElse(null) == action
	}

	def "reads transform"() {
		TransformAction action = new TransformAction(vector)

		expect:
		converter.convert("TRANSFORM($vector.x, $vector.y, $vector.z)" as String).orElse(null) == action
	}
	def "reads transform with rotation"() {
		TransformAction action = new TransformAction(vector, vector1)

		expect:
		converter.convert("TRANSFORM($vector.x, $vector.y, $vector.z | $vector1.x, $vector1.y, $vector1.z)" as String).orElse(null) == action
	}
}
