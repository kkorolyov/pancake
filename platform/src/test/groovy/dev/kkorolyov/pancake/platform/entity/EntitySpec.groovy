package dev.kkorolyov.pancake.platform.entity

import spock.lang.Specification

class EntitySpec extends Specification {
	Component component = mockComponent()

	EntityPool entities = new EntityPool()

	def "gets components by type"() {
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

	private static Component mockComponent() {
		return new Component() {}
	}
}
