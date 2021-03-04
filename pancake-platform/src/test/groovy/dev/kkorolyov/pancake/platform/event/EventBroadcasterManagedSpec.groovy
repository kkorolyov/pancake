package dev.kkorolyov.pancake.platform.event

import spock.lang.Shared
import spock.lang.Specification

import java.util.function.Consumer

class EventBroadcasterManagedSpec extends Specification {
	@Shared
	Event event = new Event() {}

	Consumer<Event> registered = Mock()
	Consumer<Event> unregistered = Mock()

	EventBroadcaster.Managed eventBroadcaster = new EventBroadcaster.Managed()

	def "broadcasts to registered receivers"() {
		eventBroadcaster.register(event.class, registered)
		eventBroadcaster.enqueue(event)

		when:
		eventBroadcaster.broadcast()

		then:
		1 * registered.accept(event)
		0 * unregistered._
	}

	def "unregisters"() {
		eventBroadcaster.register(event.class, registered)
		eventBroadcaster.enqueue(event)

		when:
		eventBroadcaster.unregister(event.class, registered)
		eventBroadcaster.broadcast()

		then:
		0 * registered._
	}

	def "supports noop unregister"() {
		eventBroadcaster.unregister(event.class, unregistered)
	}
}
