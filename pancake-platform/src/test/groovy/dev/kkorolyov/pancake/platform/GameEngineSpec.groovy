package dev.kkorolyov.pancake.platform

import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.entity.Signature
import dev.kkorolyov.pancake.platform.event.EventBroadcaster
import dev.kkorolyov.pancake.platform.utility.PerformanceCounter

import spock.lang.Specification

import java.util.function.Consumer
import java.util.stream.Stream

import static SpecUtilities.randFloat
import static SpecUtilities.setField

class GameEngineSpec extends Specification {
	float dt = randFloat()
	Signature signature = Mock()
	Comparator<UUID> comparator = Mock()
	UUID id = UUID.randomUUID()
	Stream<UUID> entityStream = Mock(Stream) {
		sequential() >> it
		forEach(_) >> {Consumer<UUID> consumer -> consumer.accept(id)}
		// TODO Spock bug with Stream.of
	}
	EventBroadcaster events = Mock()
	PerformanceCounter performanceCounter = Mock()
	EntityPool entities = Mock() {
		get(signature, comparator) >> entityStream
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
		1 * system.update(id, entities, dt)
	}
	def "does not invoke 'update' on system if no relevant entities"() {
		when:
		engine.update(dt)

		then:
		1 * entityStream.forEach(_) >> {}
		0 * system.update(_, entities, dt)
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
