package dev.kkorolyov.pancake.core.action

import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.entity.Signature
import dev.kkorolyov.pancake.platform.math.Vector

import spock.lang.Shared
import spock.lang.Specification

import static dev.kkorolyov.pancake.platform.SpecUtilities.randInt
import static dev.kkorolyov.pancake.platform.SpecUtilities.randVector

class TransformActionSpec extends Specification {
	@Shared Vector position = randVector()
	@Shared Vector rotation = randVector()
	@Shared Signature signature = new Signature(Transform)

	Vector transformPosition = Mock()
	Vector transformRotation = Mock()
	Transform transform = Mock() {
		getPosition() >> transformPosition
		getOrientation() >> transformRotation
	}
	int id = randInt()
	EntityPool entities = Mock() {
		contains(id, signature) >> true
		get(id, Transform) >> transform
	}

	TransformAction action = new TransformAction(position, rotation)

	def "sets position"() {
		when:
		action.accept(id, entities)

		then:
		1 * transformPosition.set(position)
	}

	def "sets rotation"() {
		when:
		action.accept(id, entities)

		then:
		1 * transformRotation.set(rotation)
	}
	def "does not set rotation if null"() {
		when:
		action = new TransformAction(position)
		action.accept(id, entities)

		then:
		0 * transformRotation.set(_)
	}
}
