package dev.kkorolyov.pancake.platform.entity

import dev.kkorolyov.pancake.platform.event.EventBroadcaster

import spock.lang.Specification

class EntityPoolSpec extends Specification {
	EventBroadcaster events = Mock()
	Component component = mockComponent()
	Signature signature = new Signature(component.class)

	EntityPool entities = new EntityPool(events)

	def "create provides unique ID"() {
		expect:
		entities.create(mockComponent()) != entities.create(mockComponent())
	}
	def "create adds to pool"() {
		when:
		UUID id = entities.create(component)

		then:
		++entities.get(id).iterator() == component
	}
	def "created entity has specified components"() {
		when:
		UUID id = entities.create(component)

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
		UUID id = entities.create(component)

		then:
		with(entities) {
			destroy(id) == 1
			!get(id).iterator().hasNext()
		}
	}

	def "gets entities in comparator order"() {
		UUID e1 = entities.create(component)
		UUID e2 = entities.create(component)

		Comparator<UUID> e1First = new Comparator<UUID>() {
			@Override
			int compare(UUID o1, UUID o2) {
				return o1.is(e1) ? -1 : 1
			}
		}
		Comparator<UUID> e2First = new Comparator<UUID>() {
			@Override
			int compare(UUID o1, UUID o2) {
				return o1.is(e2) ? -1 : 1
			}
		}

		expect:
		with(entities) {
			get(signature, e1First).collect() == [e1, e2]
			get(signature, e2First).collect() == [e2, e1]
		}
	}

	private static Component mockComponent() {
		return new Component() {}
	}
}
