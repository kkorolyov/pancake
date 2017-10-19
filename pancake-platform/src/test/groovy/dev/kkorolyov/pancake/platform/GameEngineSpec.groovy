package dev.kkorolyov.pancake.platform

import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.entity.Signature
import dev.kkorolyov.pancake.platform.event.EventBroadcaster
import dev.kkorolyov.pancake.platform.utility.PerformanceCounter

import spock.lang.Ignore
import spock.lang.Specification

import java.util.stream.Stream

import static SpecUtilities.randFloat
import static SpecUtilities.setField

class GameEngineSpec extends Specification {
	Signature signature = Mock()
	Comparator<Entity> comparator = Mock()
	EventBroadcaster events = Mock()
	PerformanceCounter performanceCounter = Mock()
	EntityPool entities = Mock()
	GameSystem system = Mock()
	GameSystem[] systems = (0..10).collect {it -> Mock(GameSystem) {
		getSignature() >> signature
		getComparator() >> comparator
	}}

	GameEngine engine = new GameEngine(events, entities, systems)

	def setup() {
		setField("performanceCounter", engine, performanceCounter)

		entities.get(signature, comparator) >> Mock(Stream) {
			sequential() >> it
		}
	}

	def "invokes 'before' and 'after' on each system on update"() {
		when:
		engine.update(dt)

		then:
		systems.each {
			1 * it.before(dt)
			1 * it.after(dt)
		}

		where:
		dt << randFloat()
	}

	@Ignore
	def "invokes 'update' on system for each relevant entity on update"() {
		when:
		engine.update(dt)

		then:
		entities.get(signature, comparator) >> Mock(Stream) {
			sequential() >> it
			iterator() >> Mock(Iterator) {
				next() >>> [(1..10).collect {it2 -> entity}, false]
				hasNext() >>> [(1..10).collect {it2 -> true}, false] >> { println "test"}
			}
		}
		// TODO Mocked stream is weird

		systems.each {
			1 * it.update(entity, dt)
		}

		where:
		dt << randFloat()
		entity << Mock(Entity)
	}
	def "does not invoke 'update' on system if no relevant entities"() {
		entities.get(_, _) >> []

		when:
		engine.update(dt)

		then:
		systems.each {
			0 * it.update(_, dt)
		}

		where:
		dt << randFloat()
	}

	def "shares services with added system"() {
		when:
		engine.add(system)

		then:
		1 * system.share(events, performanceCounter)
	}
	def "unshares services from removed system"() {
		when:
		engine.remove(system)

		then:
		1 * system.share(null, null)
	}

	def "invokes 'attach' on added system"() {
		when:
		engine.add(system)

		then:
		1 * system.attach()
	}
	def "invokes 'detach' on removed system"() {
		when:
		engine.remove(system)

		then:
		1 * system.detach()
	}
}
