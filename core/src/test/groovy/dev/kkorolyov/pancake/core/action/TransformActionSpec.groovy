package dev.kkorolyov.pancake.core.action

import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.math.Vector1
import dev.kkorolyov.pancake.platform.math.Vector3
import dev.kkorolyov.pancake.platform.math.Vectors

import spock.lang.Specification

import static dev.kkorolyov.pancake.platform.SpecUtilities.randVector

class TransformActionSpec extends Specification {
	Vector3 setPosition = randVector()
	Vector1 setOrientation = Vectors.create((Vector1) randVector())

	Transform component = new Transform(Vectors.create(0, 0, 0), Vectors.create(0))
	Entity entity = Mock() {
		get(Transform) >> component
	}

	Action action = new TransformAction(setPosition, setOrientation)

	def "sets position"() {
		when:
		action.apply(entity)

		then:
		component.position == setPosition
	}

	def "sets orientation"() {
		when:
		action.apply(entity)

		then:
		component.orientation == setOrientation
	}
	def "does not set orientation if null"() {
		when:
		action = new TransformAction(setPosition)
		action.apply(entity)

		then:
		component.orientation == Vectors.create(0)
	}
}
