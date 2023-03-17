package dev.kkorolyov.pancake.core.system

import dev.kkorolyov.pancake.core.component.Go
import dev.kkorolyov.pancake.core.component.Position
import dev.kkorolyov.pancake.core.component.movement.Force
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
	double buffer = 1

	@Shared
	long dt = 1

	EntityPool entities = new EntityPool()
	Go go = new Go(target, strength, buffer)
	GoSystem system = new GoSystem()

	def "pushes towards target when far away"() {
		Position position = new Position(positionV)
		Force force = new Force(Vector3.of())
		Entity entity = entities.create()
		entity.put(go, position, force)

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

	def "resets force when within target buffer"() {
		Position position = new Position(positionV)
		Force force = new Force(forceV)
		Entity entity = entities.create()
		entity.put(go, position, force)

		when:
		system.update(entity, dt)

		then:
		force.value == Vector3.of()

		where:
		positionV << [Vector3.of(1, 0), Vector3.of(0, 1), Vector3.of(0, 0, 1), Vector3.of(0.5, 0.5)]
		forceV << [Vector3.of(1, 4), Vector3.of(8, 8), Vector3.of(3), Vector3.of(4, 5, 1)]
	}

	def "removes go when within target buffer"() {
		Position position = new Position(positionV)
		Force force = new Force(Vector3.of())
		Entity entity = entities.create()
		entity.put(go, position, force)

		when:
		system.update(entity, dt)

		then:
		force.value == Vector3.of()
		entity.get(Go) == null

		where:
		positionV << [Vector3.of(1, 0), Vector3.of(0, 1), Vector3.of(0, 0, 1), Vector3.of(0.5, 0.5)]
	}
}