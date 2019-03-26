package dev.kkorolyov.pancake.platform.event.management

import dev.kkorolyov.pancake.platform.event.Event

import spock.lang.Shared
import spock.lang.Specification

import java.util.function.Consumer

import static dev.kkorolyov.simplespecs.SpecUtilities.setField

class ManagedEventBroadcasterSpec extends Specification {
	@Shared Event event = new Event() {}
	@Shared Consumer receiver = {}

	Map<Class<? extends Event>, Set<Consumer<?>>> receivers = Mock()
	HashSet<Consumer<?>> receiverSet = Mock()

	ManagedEventBroadcaster eventBroadcaster = new ManagedEventBroadcaster()

	def setup() {
		setField("receivers", eventBroadcaster, receivers)
	}

	def "registers receiver to correct event"() {
		when:
		eventBroadcaster.register(event.class, receiver)

		then:
		1 * receivers.computeIfAbsent(event.class, _) >> receiverSet
		1 * receiverSet.add(receiver)
	}

	def "unregisters receiver from correct event"() {
		when:
		eventBroadcaster.unregister(event.class, receiver)

		then:
		1 * receivers.get(event.class) >> receiverSet	// Mock event existence
		1 * receiverSet.remove(receiver)
	}
	def "does nothing when unregistering from nonexistent event"() {
		when:
		eventBroadcaster.unregister(event.class, receiver)

		then:
		1 * receivers.get(event.class)	// Mock event nonexistence
		0 * receiverSet.remove(receiver)
	}
}
