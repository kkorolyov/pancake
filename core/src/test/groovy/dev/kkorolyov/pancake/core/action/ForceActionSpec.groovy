package dev.kkorolyov.pancake.core.action

import dev.kkorolyov.pancake.core.component.movement.Force
import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.math.Vectors

import spock.lang.Specification

class ForceActionSpec extends Specification {
	Force component = new Force(Vectors.create(1, 1, 1))
	Entity entity = new EntityPool().create().with {
		it.put(component)
		it
	}

	def "adds value"() {
		Action action = new ForceAction(Vectors.create(2, 2, 2))

		when:
		action.apply(entity)

		then:
		component.value == Vectors.create(3, 3, 3)
	}
}
