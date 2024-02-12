package dev.kkorolyov.pancake.core.system

import dev.kkorolyov.pancake.core.component.Force
import dev.kkorolyov.pancake.core.component.Mass
import dev.kkorolyov.pancake.core.component.Velocity
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Specification

class AccelerationSystemSpec extends Specification {
	EntityPool entities = new EntityPool()
	AccelerationSystem system = new AccelerationSystem()

	def "accelerates"() {
		Velocity velocity = new Velocity(Vector3.of(1, 1, 1))
		Force force = new Force(Vector3.of(2, 2, 2))
		Mass mass = new Mass(2)
		Entity entity = entities.create()
		entity.put(velocity, force, mass)

		when:
		system.update(entity, 4e9 as long)

		then:
		velocity.linear == Vector3.of(5, 5, 5)
	}
}
