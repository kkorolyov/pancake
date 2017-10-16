package dev.kkorolyov.pancake.core.action

import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.Signature
import dev.kkorolyov.pancake.platform.math.Vector

import spock.lang.Shared
import spock.lang.Specification

import static dev.kkorolyov.pancake.platform.SpecUtilities.randFloat
import static dev.kkorolyov.pancake.platform.SpecUtilities.randVector

class TransformActionSpec extends Specification {
	@Shared Vector position = randVector()
	@Shared Float rotation = randFloat()
	@Shared Signature signature = new Signature(Transform)

	Vector transformPosition = Mock()
	Transform transform = Mock() {
		getPosition() >> transformPosition
	}
	Entity entity = Mock() {
		contains(signature) >> true
		get(Transform) >> transform
	}

	TransformAction action = new TransformAction(position, rotation)

	def "sets position"() {
		when:
		action.accept(entity)

		then:
		1 * transformPosition.set(position)
	}

	def "sets rotation"() {
		when:
		action.accept(entity)

		then:
		1 * transform.setRotation(rotation)
	}
	def "does not set rotation if null"() {
		when:
		action = new TransformAction(position)
		action.accept(entity)

		then:
		0 * transform.setRotation(_)
	}
}