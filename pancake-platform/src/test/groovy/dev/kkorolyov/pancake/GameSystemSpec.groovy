package dev.kkorolyov.pancake

import dev.kkorolyov.pancake.entity.Entity
import dev.kkorolyov.pancake.event.EventBroadcaster

import spock.lang.Shared
import spock.lang.Specification

import java.util.function.Consumer

import static dev.kkorolyov.pancake.SpecUtilities.setField

class GameSystemSpec extends Specification {
	@Shared String event = UUID.randomUUID().toString()
	@Shared Object payload = Mock()
	@Shared Consumer<?> receiver = {payload -> return}
	EventBroadcaster events = Mock()

	GameSystem system = new GameSystem(null) {
		@Override
		void update(Entity entity, float dt) {}
	}

	def setup() {
		setField("events", GameSystem, system, events)
	}

	def "registers event receivers to attached event broadcaster"() {
		when:
		system.register(event, receiver)

		then:
		1 * events.register(event, receiver)
	}
	def "unregisters event receivers from attached event broadcaster"() {
		when:
		system.unregister(event, receiver)

		then:
		1 * events.unregister(event, receiver)
	}

	def "queues events to attached event broadcaster"() {
		when:
		system.enqueue(event, payload)

		then:
		1 * events.enqueue(event, payload)
	}
}
