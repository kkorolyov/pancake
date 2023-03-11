package dev.kkorolyov.pancake.core.action

import dev.kkorolyov.pancake.core.component.Orientation
import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Specification

import static dev.kkorolyov.pancake.test.SpecUtilities.randVector

class OrientationActionSpec extends Specification {
	Vector3 setOrientation = randVector()

	Orientation component = new Orientation(Vector3.of(0, 0, 0))
	Entity entity = new EntityPool().create().with {
		it.put(component)
		it
	}

	Action action = new OrientationAction(setOrientation)

	def "sets orientation"() {
		when:
		action.apply(entity)

		then:
		component.value == setOrientation
	}
}
