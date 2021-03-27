package dev.kkorolyov.pancake.platform.event

import spock.lang.Shared
import spock.lang.Specification

import java.util.function.Consumer

class EventLoopBroadcastingSpec extends Specification {
	@Shared
	Event event = new Event() {}

	Consumer<Event> registered = Mock()
	Consumer<Event> unregistered = Mock()

	EventLoop.Broadcasting eventBroadcaster = new EventLoop.Broadcasting()

	def "broadcasts to registered receivers"() {
		eventBroadcaster.register(event.class, registered)
		eventBroadcaster.enqueue(event)

		when:
		eventBroadcaster.broadcast()

		then:
		1 * registered.accept(event)
		0 * unregistered._
	}
}
