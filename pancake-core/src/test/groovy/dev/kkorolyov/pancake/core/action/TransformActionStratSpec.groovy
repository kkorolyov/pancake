package dev.kkorolyov.pancake.core.action

import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.Signature
import dev.kkorolyov.pancake.platform.math.Vector

import spock.lang.Shared
import spock.lang.Specification

import static dev.kkorolyov.pancake.platform.SpecUtilities.randInt
import static dev.kkorolyov.pancake.platform.SpecUtilities.randVector

class TransformActionStratSpec extends Specification {
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
	Entity entity = Mock() {
		get(Transform) >> transform
	}

	Action action = new TransformAction(position, rotation)

	def "sets position"() {
		when:
		action.apply(entity)

		then:
		1 * transformPosition.set(position)
	}

	def "sets rotation"() {
		when:
		action.apply(entity)

		then:
		1 * transformRotation.set(rotation)
	}
	def "does not set rotation if null"() {
		when:
		action = new TransformAction(position)
		action.apply(entity)

		then:
		0 * transformRotation.set(_)
	}
}
