package dev.kkorolyov.pancake.core.system

import dev.kkorolyov.pancake.core.component.Chase
import dev.kkorolyov.pancake.core.component.Position
import dev.kkorolyov.pancake.core.component.movement.Force
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Shared
import spock.lang.Specification

class ChaseSystemSpec extends Specification {
	@Shared
	Vector3 target = Vector3.of()
	@Shared
	Vector3 strength = Vector3.of(1, 1, 1)
	@Shared
	double buffer = 1

	@Shared
	long dt = 1

	EntityPool entities = new EntityPool()

	Chase chase = new Chase(target, strength, buffer)

	ChaseSystem system = new ChaseSystem()

	def "pushes towards target when far away"() {
		Position position = new Position(positionV)
		Force force = new Force(Vector3.of())
		Entity entity = entities.create()

		entity.put(
				chase,
				position,
				force
		)

		when:
		system.update(entity, dt)

		then:
		force.value == Vector3.of(target.x < positionV.x ? -1 : 1, target.y < positionV.y ? -1 : 1, target.z < positionV.z ? -1 : 1)

		where:
		positionV << [Vector3.of(1, 1, 1), Vector3.of(1, 2, 10), Vector3.of(-1, 1, -4), Vector3.of(10, 40, -10)]
	}

	def "does nothing if within target buffer"() {
		Position position = new Position(positionV)
		Force force = new Force(Vector3.of())
		Entity entity = entities.create()

		entity.put(
				chase,
				position,
				force
		)

		when:
		system.update(entity, dt)

		then:
		force.value == Vector3.of()

		where:
		positionV << [Vector3.of(1, 0), Vector3.of(0, 1), Vector3.of(0, 0, 1), Vector3.of(0.5, 0.5)]
	}

	def "does not reduce force along axes"() {
		Position position = new Position(positionV)
		Force force = new Force(forceV)
		Entity entity = entities.create()

		entity.put(
				chase,
				position,
				force
		)

		when:
		system.update(entity, dt)

		then:
		force.value == expectedForce

		where:
		positionV << [Vector3.of(1, 1, 1), Vector3.of(-10, -4, 0)]
		forceV << [Vector3.of(-1.5, 0, 4), Vector3.of(2, -4, 4)]
		expectedForce << [Vector3.of(-1.5, -1, -1), Vector3.of(2, 1, 4)]
	}
}
