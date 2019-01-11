package dev.kkorolyov.pancake.platform

import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.entity.Signature
import dev.kkorolyov.pancake.platform.event.management.ManagedEventBroadcaster
import dev.kkorolyov.pancake.platform.utility.Limiter
import dev.kkorolyov.pancake.platform.utility.PerformanceCounter

import spock.lang.Specification

import static dev.kkorolyov.simplespecs.SpecUtilities.randInt
import static dev.kkorolyov.simplespecs.SpecUtilities.randLong
import static dev.kkorolyov.simplespecs.SpecUtilities.setField

class GameEngineSpec extends Specification {
	long dt = randLong()
	int id = randInt()
	Signature signature = new Signature(Object)
	ManagedEventBroadcaster events = Mock()
	PerformanceCounter performanceCounter = Mock()
	EntityPool entities = new EntityPool(events)
	GameSystem deadSystem = Mock() {
		getSignature() >> signature
		getLimiter() >> Mock(Limiter)
		setResources() >> it
	}
	GameSystem readySystem = Mock() {
		getSignature() >> signature
		getLimiter() >> Mock(Limiter) {
			isReady(_) >> true
			consumeElapsed() >> dt
		}
		setResources(_) >> it
	}

	GameEngine engine = new GameEngine(events, entities, readySystem)

	def setup() {
		setField("performanceCounter", engine, performanceCounter)
	}

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
		1 * readySystem.update(id, dt)
	}
	def "does not invoke 'update' on system if no relevant entities"() {
		when:
		engine.update(dt)

		then:
		1 * entities.forEachMatching(signature, _) >> {}
		0 * readySystem.update(_, dt)
	}

	def "shares services with added system"() {
		when:
		engine.add(readySystem)

		then:
		1 * readySystem.setResources(new SharedResources(events, performanceCounter))
	}
	def "unshares services from removed system"() {
		when:
		engine.remove(readySystem)

		then:
		1 * readySystem.setResources(null)
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
}
