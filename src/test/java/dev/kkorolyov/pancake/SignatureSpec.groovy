package dev.kkorolyov.pancake

import dev.kkorolyov.pancake.component.*
import spock.lang.Shared
import spock.lang.Specification

import java.lang.reflect.Field

class SignatureSpec extends Specification {
	@Shared
	List<Class<? extends Component>> componentTypes = [
			Bounds,
			Damping,
			Force,
			MaxSpeed,
			Sprite,
			Transform,
			Velocity
	]

	Signature signature = new Signature()

	def "empty signature equals zero"() {
		signature = new Signature()

		expect:
		getSignatureField() == 0
	}

	private long getSignatureField() {
		Field f = Signature.getDeclaredField("signature")
		f.setAccessible(true)

		return f.get(signature) as long
	}

	def "contains all constructor-initialized component types"() {
		when:
		signature = new Signature(toArray(componentTypes))

		then:
		signature.contains(toArray(componentTypes))
	}

	def "symmetrically contains signature with matching component types"() {
		when:
		signature = new Signature(toArray(componentTypes))
		Signature signature2 = new Signature(toArray(componentTypes))

		then:
		signature.contains(signature2)
		signature2.contains(signature)
	}
	def "contains signature with subset of component types"() {
		signature = new Signature(toArray(componentTypes))
		Signature half0 = new Signature(split(componentTypes, 2, 0))
		Signature half1 = new Signature(split(componentTypes, 2, 1))

		expect:
		signature.contains(half0)
		signature.contains(half1)

		!half0.contains(signature)
		!half1.contains(signature)
	}

	def "contains all added component types"() {
		List<Class<? extends Component>> addedTypes = []

		expect:
		!signature.contains(type)

		when:
		signature.add(type)
		addedTypes.push(type)

		then:
		addedTypes.each { signature.contains(it) }
		signature.contains(toArray(addedTypes))

		where:
		type << componentTypes
	}
	def "adding present component type changes nothing"() {
		when:
		signature.add(type)
		then:
		signature.contains(type)

		when:
		signature.add(type)
		then:
		signature.contains(type)

		where:
		type << componentTypes
	}

	def "does not contain removed component type"() {
		when:
		signature.add(type)
		then:
		signature.contains(type)

		when:
		signature.remove(type)
		then:
		!signature.contains(type)

		where:
		type << componentTypes
	}
	def "removing missing component type changes nothing"() {
		when:
		signature.remove(type)
		then:
		!signature.contains(type)

		where:
		type << componentTypes
	}

	def "signatures with same component types equal"() {
		when:
		Signature signature2 = new Signature()
		then:
		signature == signature2

		when:
		signature.add(type)
		signature2.add(type)
		then:
		signature == signature2

		where:
		type << componentTypes
	}
	def "signatures with different component types unequal"() {
		Signature signature2 = new Signature()

		when:
		signature.add(type1)
		signature2.add(type2)

		then:
		signature != signature2

		where:
		type1 << split(componentTypes, 2, 0)
		type2 << split(componentTypes, 2, 1)
	}

	private static Class<? extends Component>[] toArray(List<Class<? extends Component>> list) {
		return list.toArray(new Class<? extends Component>[list.size()])
	}

	private static Class<? extends Component>[] split(List<Class<? extends Component>> list, int partitions, int partition) {
		return list.collate(list.size() / partitions as int)[partition]
	}
}
