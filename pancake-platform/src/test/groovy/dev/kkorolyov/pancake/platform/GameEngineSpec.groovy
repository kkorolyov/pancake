package dev.kkorolyov.pancake.platform

import dev.kkorolyov.pancake.platform.entity.ManagedEntityPool
import dev.kkorolyov.pancake.platform.entity.Signature
import dev.kkorolyov.pancake.platform.event.management.ManagedEventBroadcaster
import dev.kkorolyov.pancake.platform.utility.PerformanceCounter

import spock.lang.Specification

import java.util.function.Consumer

import static SpecUtilities.randFloat
import static SpecUtilities.setField
import static dev.kkorolyov.pancake.platform.SpecUtilities.randInt

class GameEngineSpec extends Specification {
	float dt = randFloat()
	Signature signature = Mock()
	Comparator<UUID> comparator = Mock()
	int id = randInt()
	ManagedEventBroadcaster events = Mock()
	PerformanceCounter performanceCounter = Mock()
	ManagedEntityPool entities = Mock() {
		forEachMatching(signature, _) >> { Signature signature, Consumer<Integer> consumer -> consumer.accept(id) }
	}
	GameSystem system = Mock() {
		getSignature() >> signature
		getComparator() >> comparator
	}

	GameEngine engine = new GameEngine(events, entities, system)

	def setup() {
		setField("performanceCounter", engine, performanceCounter)
	}

	def "invokes 'before' and 'after' on systems on update"() {
		when:
		engine.update(dt)

		then:
		with(system) {
			1 * before(dt)
			1 * after(dt)
		}
	}

	def "invokes 'update' on system for each relevant entity on update"() {
		when:
		engine.update(dt)

		then:
		1 * system.update(id, dt)
	}
	def "does not invoke 'update' on system if no relevant entities"() {
		when:
		engine.update(dt)

		then:
		1 * entities.forEachMatching(signature, _) >> {}
		0 * system.update(_, dt)
	}

	def "shares services with added system"() {
		when:
		engine.add(system)

		then:
		1 * system.share(entities, events, performanceCounter)
	}
	def "unshares services from removed system"() {
		when:
		engine.remove(system)

		then:
		1 * system.share(null, null, null)
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
