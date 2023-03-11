package dev.kkorolyov.pancake.core.system

import dev.kkorolyov.pancake.core.component.Position
import dev.kkorolyov.pancake.core.component.movement.Velocity
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Specification

class MovementSystemSpec extends Specification {
	EntityPool entities = new EntityPool()
	MovementSystem system = new MovementSystem()

	def "moves"() {
		Position position = new Position(Vector3.of(1, 1, 1))
		Velocity velocity = new Velocity(Vector3.of(2, 2, 2))
		Entity entity = entities.create()
		entity.put(position, velocity)

		when:
		system.update(entity, 3e9 as long)

		then:
		position.value == Vector3.of(7, 7, 7)
	}
}
