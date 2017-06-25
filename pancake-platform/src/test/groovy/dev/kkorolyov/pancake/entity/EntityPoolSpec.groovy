package dev.kkorolyov.pancake.entity

import dev.kkorolyov.pancake.event.EventBroadcaster
import spock.lang.Shared
import spock.lang.Specification

class EntityPoolSpec extends Specification {
	@Shared EventBroadcaster events = Mock()
	@Shared Component component = new Component() {}
	@Shared Signature signature
	@Shared Comparator<Entity> increasing = new Comparator<Entity>() {
		@Override
		int compare(Entity e1, Entity e2) {
			return e1.getId() <=> e2.getId()
		}
	}
	@Shared Comparator<Entity> decreasing = new Comparator<Entity>() {
		@Override
		int compare(Entity e1, Entity e2) {
			return e2.getId() <=> e1.getId()
		}
	}

	EntityPool entities = new EntityPool(events)

	def setupSpec() {
		Signature.index(component.class)

		signature = new Signature(component.class)
	}

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

		expect:
		entities.get(signature, increasing).collect() == [e1, e2]
		entities.get(signature, decreasing).collect() == [e2, e1]
	}
}
