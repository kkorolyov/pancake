package dev.kkorolyov.pancake.core.system

import dev.kkorolyov.pancake.core.component.Go
import dev.kkorolyov.pancake.core.component.Path
import dev.kkorolyov.pancake.core.component.Position
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Shared
import spock.lang.Specification

class PathSystemSpec extends Specification {
	@Shared
	long dt = 1

	double strength = 40
	double buffer = 0

	EntityPool entities = new EntityPool()
	PathSystem system = new PathSystem()

	def "moves towards next step if entity has no Go component"() {
		Position position = new Position(positionV)
		Path path = new Path(strength, buffer)
		path.add(next)
		Entity entity = entities.create()
		entity.put(position, path)

		when:
		system.update(entity, dt)

		then:
		entity.get(Go).target == next
		entity.get(Go).strength == strength
		entity.get(Go).buffer == buffer
		!path.hasNext()

		where:
		positionV << [Vector3.of(), Vector3.of(4, 8, 24)]
		next << [Vector3.of(1), Vector3.of(17, 14, 5)]
	}

	def "does not set next if entity has Go component"() {
		Position position = new Position(positionV)
		Go go = new Go(targetV, 1, 0)
		Path path = new Path(strength, buffer)
		path.add(next)
		Entity entity = entities.create()
		entity.put(position, go, path)

		when:
		system.update(entity, dt)

		then:
		go.target == targetV
		path.hasNext()

		where:
		positionV << [Vector3.of(), Vector3.of(4, 8, 24)]
		targetV << [Vector3.of(4), Vector3.of(4, 8, 25)]
		next << [Vector3.of(1), Vector3.of(17, 14, 5)]
	}

	def "does not set next if no more steps"() {
		Position position = new Position(positionV)
		Path path = new Path(strength, buffer)
		Entity entity = entities.create()
		entity.put(position, path)

		when:
		system.update(entity, dt)

		then:
		entity.get(Go) == null
		!path.hasNext()

		where:
		positionV << [Vector3.of(), Vector3.of(4, 8, 24)]
		targetV << [Vector3.of(4), Vector3.of(4, 8, 25)]
	}
}
