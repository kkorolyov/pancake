package dev.kkorolyov.pancake.platform

import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.entity.Signature
import dev.kkorolyov.pancake.platform.event.EventLoop
import dev.kkorolyov.pancake.platform.utility.Limiter

import spock.lang.Specification

import static dev.kkorolyov.pancake.platform.SpecUtilities.randLong

class GameEngineSpec extends Specification {
	long dt = randLong()
	Signature signature = new Signature(MockComponent)

	EventLoop.Broadcasting events = new EventLoop.Broadcasting()
	EntityPool entities = new EntityPool(events)
	Entity entity = entities.create().with {
		it.put(new MockComponent())
		it
	}

	GameSystem deadSystem = Spy(new GameSystem(signature, new Limiter(Long.MAX_VALUE)) {
		@Override
		void update(Entity entity, long dt) {
		}
	})
	GameSystem readySystem = Spy(new GameSystem(signature, new Limiter(0)) {
		@Override
		void update(Entity entity, long dt) {
		}
	})

	GameEngine engine = new GameEngine(events, entities, [readySystem, deadSystem])

	def "invokes 'before' and 'after' on ready systems on update"() {
		when:
		engine.update(dt)

		then:
		with(deadSystem) {
			0 * before(dt)
			0 * after(dt)
		}
		with(readySystem) {
			1 * before(dt)
			1 * after(dt)
		}
	}

	def "invokes 'update' on system for each relevant entity on update"() {
		when:
		engine.update(dt)

		then:
		1 * readySystem.update(entity, dt)
		0 * deadSystem.update(_, _)
	}
	def "does not invoke 'update' on system if no relevant entities"() {
		GameSystem system = Spy(new GameSystem(new Signature(new Component() {}.class), new Limiter(0)) {
			@Override
			void update(Entity entity, long dt) {
			}
		})

		when:
		engine.update(dt)

		then:
		0 * system.update(_, _)
		0 * deadSystem.update(_, _)
	}

	def "invokes 'attach' on added system"() {
		when:
		engine.add(readySystem)

		then:
		1 * readySystem.attach()
	}
	def "invokes 'detach' on removed system"() {
		when:
		engine.remove(readySystem)

		then:
		1 * readySystem.detach()
	}

	class MockComponent implements Component {}
}
