package dev.kkorolyov.pancake.platform

import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.entity.Signature
import dev.kkorolyov.pancake.platform.event.EventLoop
import dev.kkorolyov.pancake.platform.plugin.GameSystem
import dev.kkorolyov.pancake.platform.plugin.RenderMedium
import dev.kkorolyov.pancake.platform.utility.DebugRenderer
import dev.kkorolyov.pancake.platform.utility.Limiter

import spock.lang.Specification

import static dev.kkorolyov.pancake.platform.SpecUtilities.randLong

class GameEngineSpec extends Specification {
	long dt = randLong()
	Signature signature = new Signature(MockComponent)

	EventLoop.Broadcasting events = new EventLoop.Broadcasting()
	DebugRenderer debugRenderer = new DebugRenderer(Mock(RenderMedium))
	EntityPool entities = new EntityPool(events)
	Entity entity = entities.create().with {
		it.add(new MockComponent())
		it
	}

	GameSystem deadSystem = Mock() {
		getSignature() >> signature
		getLimiter() >> new Limiter(Long.MAX_VALUE)
	}
	GameSystem readySystem = Mock() {
		getSignature() >> signature
		getLimiter() >> new Limiter(0)
	}

	GameEngine engine = new GameEngine(events, entities, [readySystem, deadSystem], debugRenderer)

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
		when:
		engine.update(dt)

		then:
		1 * readySystem.getSignature() >> new Signature(new Component() {}.class)

		0 * readySystem.update(_, _)
		0 * deadSystem.update(_, _)
	}

	def "shares services with added system"() {
		when:
		engine.add(readySystem)

		then:
		1 * readySystem.setEvents(!null)
	}
	def "unshares services from removed system"() {
		when:
		engine.remove(readySystem)

		then:
		1 * readySystem.setEvents(null)
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
