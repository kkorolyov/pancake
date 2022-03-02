package dev.kkorolyov.pancake.core.action

import dev.kkorolyov.pancake.core.component.movement.Velocity
import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.math.Vector3
import dev.kkorolyov.pancake.platform.math.Vectors

import spock.lang.Specification

import static dev.kkorolyov.pancake.platform.SpecUtilities.randVector

class VelocityActionSpec extends Specification {
	Vector3 set = randVector()

	Velocity component = new Velocity(Vectors.create(0, 0, 0))
	Entity entity = new EntityPool().create().with {
		it.put(component)
		it
	}

	Action action = new VelocityAction(set)

	def "sets value"() {
		when:
		action.apply(entity)

		then:
		component.value == set
	}
}
