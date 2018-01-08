package dev.kkorolyov.pancake.platform.entity

import dev.kkorolyov.pancake.platform.event.EventBroadcaster

import spock.lang.Specification

class EntityPoolSpec extends Specification {
	EventBroadcaster events = Mock()
	Component component = Mock()
	Signature signature = new Signature(component.class)

	EntityPool entities = new EntityPool(events)

	def "uses unique IDs"() {
		expect:
		entities.create(component).getId() != entities.create(component).getId()
	}
	def "reuses IDs of destroyed entities"() {
		Entity entity = entities.create(component)

		when:
		entities.destroy(entity)

		then:
		entities.create(component).getId() == entity.getId()
	}

	def "created entity has specified components"() {
		Entity entity = entities.create(component)

		expect:
		entity.get(component.class) == component
		entity.get(component.class) != new Component() {}
		entity.get(new Component() {}.class) == null
	}

	def "gets entities in comparator order"() {
		Entity e1 = entities.create(component)
		Entity e2 = entities.create(component)

		Comparator<Entity> e1First = new Comparator<Entity>() {
			@Override
			int compare(Entity o1, Entity o2) {
				return o1.is(e1) ? -1 : 1
			}
		}
		Comparator<Entity> e2First = new Comparator<Entity>() {
			@Override
			int compare(Entity o1, Entity o2) {
				return o1.is(e2) ? -1 : 1
			}
		}

		expect:
		entities.get(signature, e1First).collect() == [e1, e2]
		entities.get(signature, e2First).collect() == [e2, e1]
	}
}
