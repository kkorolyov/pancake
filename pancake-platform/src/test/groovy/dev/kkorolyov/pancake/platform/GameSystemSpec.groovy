package dev.kkorolyov.pancake.platform

import dev.kkorolyov.pancake.platform.entity.Signature
import dev.kkorolyov.pancake.platform.event.EventBroadcaster

import spock.lang.Specification

import java.util.function.Consumer

import static SpecUtilities.setField

class GameSystemSpec extends Specification {
	String event = UUID.randomUUID().toString()
	Object payload = Mock()
	Consumer<?> receiver = {payload -> return}
	EventBroadcaster events = Mock()
	Signature signature = Mock()

	GameSystem system = Spy(constructorArgs: [signature])

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
