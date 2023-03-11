package dev.kkorolyov.pancake.core.action

import dev.kkorolyov.pancake.core.component.Position
import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Specification

import static dev.kkorolyov.pancake.test.SpecUtilities.randVector

class PositionActionSpec extends Specification {
	Vector3 setPosition = randVector()

	Position component = new Position(Vector3.of(0, 0, 0))
	Entity entity = new EntityPool().create().with {
		it.put(component)
		it
	}

	Action action = new PositionAction(setPosition)

	def "sets position"() {
		when:
		action.apply(entity)

		then:
		component.value == setPosition
	}
}
