package dev.kkorolyov.pancake.core.system

import dev.kkorolyov.pancake.core.component.Force
import dev.kkorolyov.pancake.core.component.Go
import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.core.component.Velocity
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Shared
import spock.lang.Specification

class GoSystemSpec extends Specification {
	@Shared
	Vector3 target = Vector3.of()
	@Shared
	double strength = 1
	@Shared
	double proximity = 1

	@Shared
	long dt = 1

	EntityPool entities = new EntityPool()
	Go go = new Go(target, strength, proximity, false)
	GoSystem system = new GoSystem()

	def "pushes towards target when far away"() {
		Transform transform = new Transform().with {
			it.translation.set(positionV)
			it
		}
		Force force = new Force()
		Entity entity = entities.create()
		entity.put(go, transform, force)

		when:
		system.update(entity, dt)

		then:
		force.value == Vector3.of(0, 0, 0).with {
			it.add(positionV, -1)
			it.normalize()
			it
		}

		where:
		positionV << [Vector3.of(1, 1, 1), Vector3.of(1, 2, 10), Vector3.of(-1, 1, -4), Vector3.of(10, 40, -10)]
	}

	def "resets force when within target proximity"() {
		Transform transform = new Transform().with {
			it.translation.set(positionV)
			it
		}
		Force force = new Force().with {
			it.value.set(forceV)
			it
		}
		Entity entity = entities.create()
		entity.put(go, transform, force)

		when:
		system.update(entity, dt)

		then:
		force.value == Vector3.of()

		where:
		positionV << [Vector3.of(1, 0), Vector3.of(0, 1), Vector3.of(0, 0, 1), Vector3.of(0.5, 0.5)]
		forceV << [Vector3.of(1, 4), Vector3.of(8, 8), Vector3.of(3), Vector3.of(4, 5, 1)]
	}

	def "does not snap position nor zero velocity when within target proximity if not snap set"() {
		Transform transform = new Transform().with {
			it.translation.set(positionV)
			it
		}
		Force force = new Force()
		Velocity velocity = new Velocity().with {
			it.linear.set(Vector3.of(1.0, 1.0))
			it
		}
		Entity entity = entities.create()
		entity.put(go, transform, force, velocity)

		when:
		system.update(entity, dt)

		then:
		transform.translation != go.target
		velocity.linear == Vector3.of(1.0, 1.0)

		where:
		positionV << [Vector3.of(1, 0), Vector3.of(0, 1), Vector3.of(0, 0, 1), Vector3.of(0.5, 0.5)]
	}
	def "snaps position and zeroes velocity when within target proximity if snap set"() {
		Transform transform = new Transform().with {
			it.translation.set(positionV)
			it
		}
		Force force = new Force()
		Velocity velocity = new Velocity().with {
			it.linear.set(Vector3.of(1.0, 1.0))
			it
		}
		go.snap = true
		Entity entity = entities.create()
		entity.put(go, transform, force, velocity)

		when:
		system.update(entity, dt)

		then:
		transform.translation == go.target
		velocity.linear == Vector3.of()

		where:
		positionV << [Vector3.of(1, 0), Vector3.of(0, 1), Vector3.of(0, 0, 1), Vector3.of(0.5, 0.5)]
	}

	def "removes go when within target proximity"() {
		Transform transform = new Transform().with {
			it.translation.set(positionV)
			it
		}
		Force force = new Force()
		Entity entity = entities.create()
		entity.put(go, transform, force)

		when:
		system.update(entity, dt)

		then:
		entity.get(Go) == null

		where:
		positionV << [Vector3.of(1, 0), Vector3.of(0, 1), Vector3.of(0, 0, 1), Vector3.of(0.5, 0.5)]
	}
}
