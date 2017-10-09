package dev.kkorolyov.pancake.platform.entity

import dev.kkorolyov.pancake.platform.action.Action

import spock.lang.Shared
import spock.lang.Specification

class EntitySpec extends Specification {
	@Shared int id = 0
	@Shared List<Component> components = [new Component() {}, new Component() {}, new Component() {}]
	Action action = Mock()

	Entity entity = new Entity(id)

	def "matches signature of all constructor-initialized component types"() {
		when:
		entity = new Entity(id, components)
		then:
		entity.matches(new Signature(mapToTypes(components)))
	}

	def "contains signature with subset of component types"() {
		entity = new Entity(id, components)
		Signature equal = new Signature(mapToTypes(components))
		Signature half0 = new Signature(mapToTypes(split(components, 2, 0)))
		Signature half1 = new Signature(mapToTypes(split(components, 2, 1)))

		expect:
		entity.contains(equal)
		entity.contains(half0)
		entity.contains(half1)
	}

	def "retrieves added component by type"() {
		expect:
		entity.get(component.getClass()) == null

		when:
		entity.add(component)
		then:
		entity.get(component.getClass()) == component

		where:
		component << components
	}

	def "does not retrieve removed component by type"() {
		when:
		entity.add(component)
		then:
		entity.get(component.getClass()) == component

		when:
		entity.remove(component.getClass())
		then:
		entity.get(component.getClass()) == null

		where:
		component << components
	}

	def "applies attached actions"() {
		List<Action> actions = (0..3).collect {Mock(Action)}

		actions.each {entity.add(it)}

		expect:
		entity.applyActions() == actions.size()
		actions.each {it.accept(entity)}
	}

	def "removes attached actions after apply"() {
		entity.add(action)

		when:
		int result0 = entity.applyActions()
		int result1 = entity.applyActions()

		then:
		result0 == 1
		result1 == 0
	}

	private static List<Component> split(List<Component> list, int partitions, int partition) {
		return list.collate(list.size() / partitions as int)[partition]
	}

	private static List<Class<? extends Component>> mapToTypes(List<Component> components) {
		return components.collect { it.getClass() }.toArray(new Class<? extends Component>[components.size()])
	}
}
