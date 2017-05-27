package dev.kkorolyov.pancake.entity

import dev.kkorolyov.pancake.Component
import dev.kkorolyov.pancake.Signature
import dev.kkorolyov.pancake.component.*
import spock.lang.Shared
import spock.lang.Specification

class EntitySpec extends Specification {
	@Shared int id = 0
	@Shared List<Component> components = [
			Mock(Bounds),
			Mock(Damping),
			Mock(Force),
			Mock(MaxSpeed),
			Mock(Sprite),
			Mock(Transform),
			Mock(Velocity)
	]

	def setupSpec() {
		Signature.index(mapToTypes(components))
	}

	Entity entity = new Entity(id)

	def "contains all constructor-initialized component types"() {
		when:
		entity = new Entity(id, toArray(components))
		then:
		entity.contains(mapToTypes(components))
	}
	def "contains signature with subset of component types"() {
		entity = new Entity(id, toArray(components))
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

	private static Component[] toArray(List<Component> list) {
		return list.toArray(new Component[list.size()])
	}

	private static List<Component> split(List<Component> list, int partitions, int partition) {
		return list.collate(list.size() / partitions as int)[partition]
	}

	private static Class<? extends Component>[] mapToTypes(List<Component> components) {
		return components.collect { it.getClass() }.toArray(new Class<? extends Component>[components.size()])
	}
}
