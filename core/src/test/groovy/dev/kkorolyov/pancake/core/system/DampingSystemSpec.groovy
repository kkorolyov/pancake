package dev.kkorolyov.pancake.core.system

import dev.kkorolyov.pancake.core.component.Damping
import dev.kkorolyov.pancake.core.component.Force
import dev.kkorolyov.pancake.core.component.Velocity
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Shared
import spock.lang.Specification

class DampingSystemSpec extends Specification {
	@Shared
	Vector3 value = Vector3.of(0.5, 0.5, 0.5)
	@Shared
	double mini = 0.001
	@Shared
	double micro = 0.000001

	@Shared
	long dt = 1

	EntityPool entities = new EntityPool()
	Damping damping = new Damping(value, value)
	DampingSystem system = new DampingSystem()

	def "damps when force zero"() {
		Velocity velocity = new Velocity().with {
			it.linear.set(Vector3.of(1, 1, 1))
			it
		}
		Force force = new Force()
		Entity entity = entities.create()
		entity.put(damping, velocity, force)

		when:
		system.update(entity, dt)

		then:
		velocity.linear == value
	}
	def "damps where force opposite sign of velocity"() {
		Velocity velocity = new Velocity().with {
			it.linear.set(velocityV)
			it
		}
		Force force = new Force().with {
			it.value.set(forceV)
			it
		}
		Entity entity = entities.create()
		entity.put(damping, velocity, force)

		when:
		system.update(entity, dt)

		then:
		velocity.linear == Vector3.of((forceV.x < 0) ? value.x * mini : mini, (forceV.y < 0) ? value.y * mini : mini, (forceV.z < 0) ? value.z * mini : mini)

		where:
		velocityV << (1..3).collect { it -> Vector3.of(mini, mini, mini) }
		forceV << [Vector3.of(-micro, micro, micro), Vector3.of(micro, -micro, micro), Vector3.of(micro, micro, -micro)]
	}
	def "does not damp where force non-zero and same sign as velocity"() {
		Velocity velocity = new Velocity().with {
			it.linear.set(velocityV)
			it
		}
		Force force = new Force().with {
			it.value.set(forceV)
			it
		}
		Entity entity = entities.create()
		entity.put(damping, velocity, force)

		when:
		system.update(entity, dt)

		then:
		velocity.linear == Vector3.of((forceV.x > 0) ? mini : value.x * mini, (forceV.y > 0) ? mini : value.y * mini, (forceV.z > 0) ? mini : value.z * mini)

		where:
		velocityV << (1..3).collect { it -> Vector3.of(mini, mini, mini) }
		forceV << [Vector3.of(micro, 0, 0), Vector3.of(0, micro, 0), Vector3.of(0, 0, micro)]
	}
}
