package dev.kkorolyov.pancake.platform.entity

import dev.kkorolyov.pancake.platform.event.EventBroadcaster

import spock.lang.Specification

class ManagedEntityPoolSpec extends Specification {
	EventBroadcaster events = Mock()
	Component component = mockComponent()
	Signature signature = new Signature(component.class)

	ManagedEntityPool entities = new ManagedEntityPool(events)

	def "create provides unique ID"() {
		expect:
		entities.create(mockComponent()) != entities.create(mockComponent())
	}
	def "create adds to pool"() {
		when:
		int id = entities.create(component)

		then:
		++entities.get(id).iterator() == component
	}
	def "created entity has specified components"() {
		when:
		int id = entities.create(component)

		then:
		Component otherComponent = new Component() {}

		with(entities) {
			get(id, component.class) == component
			get(id, component.class) != mockComponent()
			get(id, otherComponent.class) == null
		}
	}

	def "destroy removes entity"() {
		when:
		int id = entities.create(component)

		then:
		with(entities) {
			destroy(id) == 1
			!get(id).iterator().hasNext()
		}
	}

	def "invokes on each matching entity"() {
		int e1 = entities.create(component)
		int e2 = entities.create(component)
		int eBad = entities.create(new Component() {})

		Set<Integer> seen = []

		when:
		entities.forEachMatching(signature, { seen.add(it) })

		then:
		seen == [e1, e2].toSet()
	}

	private static Component mockComponent() {
		return new Component() {}
	}
}
