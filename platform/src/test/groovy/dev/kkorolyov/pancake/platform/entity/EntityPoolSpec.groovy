package dev.kkorolyov.pancake.platform.entity

import dev.kkorolyov.pancake.platform.event.EventLoop

import spock.lang.Specification

class EntityPoolSpec extends Specification {
	EventLoop events = Mock()
	Component component = mockComponent()
	Signature signature = new Signature(component.class)

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
		entity.add(component)

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
		e1.add(component)

		Entity e2 = entities.create()
		e2.add(component)

		Entity eBad = entities.create()
		eBad.add(new Component() {})

		Set<Entity> seen = []

		when:
		entities.stream(signature).forEach({ seen.add(it) })

		then:
		seen == [e1, e2].toSet()
	}

	private static Component mockComponent() {
		return new Component() {}
	}
}
