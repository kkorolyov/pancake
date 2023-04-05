package dev.kkorolyov.pancake.platform.entity

import spock.lang.Specification

class EntityPoolSpec extends Specification {
	Component component = new Component() {}

	EntityPool entities = new EntityPool()

	def "create provisions unique ID"() {
		expect:
		entities.create() != entities.create()
	}
	def "create adds to pool"() {
		expect:
		entities.get(entities.create().id)
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
		entities.get([component.class]) as Set == [e1, e2].toSet()
	}
}
