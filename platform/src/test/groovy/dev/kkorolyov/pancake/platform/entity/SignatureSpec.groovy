package dev.kkorolyov.pancake.platform.entity

import spock.lang.Shared
import spock.lang.Specification

class SignatureSpec extends Specification {
	@Shared
	List<Class<? extends Component>> componentTypes = [new Component() {}, new Component() {}, new Component() {}].collect { it.class }

	def "masks empty signature"() {
		expect:
		new Signature(componentTypes).masks(new Signature())
	}

	def "masks all component types"() {
		expect
		new Signature(componentTypes).masks(new Signature(componentTypes))
	}

	def "symmetrically masks signature with matching component types"() {
		when:
		Signature signature = new Signature(componentTypes)
		Signature signature2 = new Signature(componentTypes)

		then:
		signature.masks(signature2)
		signature2.masks(signature)
	}
	def "masks signature with subset of component types"() {
		Signature signature = new Signature(componentTypes)
		Signature half0 = new Signature(split(componentTypes, 2, 0))
		Signature half1 = new Signature(split(componentTypes, 2, 1))

		expect:
		signature.masks(half0)
		signature.masks(half1)

		!half0.masks(signature)
		!half1.masks(signature)
	}

	def "empty signatures equal"() {
		expect:
		new Signature() == new Signature()
	}
	def "signatures with same component types equal"() {
		expect:
		new Signature(type) == new Signature(type)

		where:
		type << componentTypes
	}
	def "signatures with different component types unequal"() {
		expect:
		new Signature(type1) != new Signature(type2)

		where:
		type1 << split(componentTypes, 2, 0)
		type2 << split(componentTypes, 2, 1)
	}

	private static List<Class<? extends Component>> split(List<Class<? extends Component>> list, int partitions, int partition) {
		return list.collate(list.size() / partitions as int)[partition]
	}
}
