package dev.kkorolyov.pancake.core.system

import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.core.component.Velocity
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.math.Matrix4
import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Specification

class MovementSystemSpec extends Specification {
	EntityPool entities = new EntityPool()
	MovementSystem system = new MovementSystem()

	def "moves"() {
		Transform transform = new Transform().with {
			it.translation.set(Vector3.of(1, 1, 1))
			it
		}
		Velocity velocity = new Velocity().with {
			it.linear.set(Vector3.of(2, 2, 2))
			it.angular.set(Vector3.of(1, 1, 1))
			it
		}
		Entity entity = entities.create()
		entity.put(transform, velocity)

		when:
		system.update(entity, 3e9 as long)

		then:
		transform.translation == Vector3.of(7, 7, 7)
		transform.rotation == Matrix4.of().with {
			it.rotate(3, Vector3.of(1))
			it.rotate(3, Vector3.of(0, 1))
			it.rotate(3, Vector3.of(0, 0, 1))
			it
		}
	}
}
