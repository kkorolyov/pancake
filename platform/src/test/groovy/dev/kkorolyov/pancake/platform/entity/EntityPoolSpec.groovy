package dev.kkorolyov.pancake.platform.entity

import dev.kkorolyov.pancake.platform.event.EventLoop

import spock.lang.Specification

class EntityPoolSpec extends Specification {
	EventLoop events = Mock()
	Component component = mockComponent()
	Collection<Class<? extends Component>> signature = List.of(component.class)

	EntityPool entities = new EntityPool(events)

	def "create provisions unique ID"() {
		expect:
		entities.create() != entities.create()
	}
	def "create adds to pool"() {
		expect:
		entities.get(entities.create().id)
	}
	def "created entity has specified components"() {
		when:
		Entity entity = entities.create()
		entity.put(component)

		then:
		Component otherComponent = new Component() {}

		with(entity) {
			get(component.class) == component
			get(component.class) != mockComponent()
			!get(otherComponent.class)
		}
	}

	def "destroy removes entity"() {
		when:
		int id = entities.create().id
		entities.destroy(id)

		then:
		!entities.get(id)
	}

	def "invokes on each matching entity"() {
		Entity e1 = entities.create()
		e1.put(component)

		Entity e2 = entities.create()
		e2.put(component)

		Entity eBad = entities.create()
		eBad.put(new Component() {})

		expect:
		entities.get(signature) as Set == [e1, e2].toSet()
	}

	private static Component mockComponent() {
		return new Component() {}
	}
}
