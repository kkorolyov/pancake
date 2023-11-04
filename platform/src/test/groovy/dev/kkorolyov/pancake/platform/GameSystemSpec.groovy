package dev.kkorolyov.pancake.platform

import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.EntityPool

import spock.lang.Shared
import spock.lang.Specification

class GameSystemSpec extends Specification {
	@Shared
	static long dt = 10

	EntityPool entities = new EntityPool()

	Entity[] matchedEntities = (1..4).collect {
		Entity result = entities.create()
		result.put(new MockComponent())
		result
	}

	GameSystem system = Spy(MockSystem)

	def setup() {
		(1..4).forEach {
			Entity result = entities.create()
			result.put(new Component() {})
			result
		}
		system.attach(entities)
	}

	def "runs before"() {
		when:
		system.update(dt)

		then:
		1 * system.before(dt)
	}
	def "runs after"() {
		when:
		system.update(dt)

		then:
		1 * system.after(dt)
	}

	def "updates matching entities"() {
		when:
		system.update(dt)

		then:
		matchedEntities.each {
			1 * system.update(it, dt)
		}
		0 * system.update(_, _)
	}

	private static class MockComponent implements Component {}

	private static class MockSystem extends GameSystem {
		protected MockSystem() {
			super(MockComponent)
		}

		@Override
		protected void update(Entity entity, long dt) {}
	}
}
