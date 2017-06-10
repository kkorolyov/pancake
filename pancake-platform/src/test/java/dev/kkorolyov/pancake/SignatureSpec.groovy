package dev.kkorolyov.pancake

import dev.kkorolyov.pancake.entity.Component
import dev.kkorolyov.pancake.entity.Signature
import spock.lang.Shared
import spock.lang.Specification

import java.lang.reflect.Field

class SignatureSpec extends Specification {
	@Shared
	List<Class<? extends Component>> componentTypes = [new Component() {}, new Component() {}, new Component() {}].collect { it.class }

	def setupSpec() {
		Signature.index(componentTypes)
	}

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

	def "masks all constructor-initialized component types"() {
		when:
		signature = new Signature(componentTypes)

		then:
		signature.masks(new Signature(componentTypes))
	}

	def "symmetrically masks signature with matching component types"() {
		when:
		signature = new Signature(componentTypes)
		Signature signature2 = new Signature(componentTypes)

		then:
		signature.masks(signature2)
		signature2.masks(signature)
	}
	def "masks signature with subset of component types"() {
		signature = new Signature(componentTypes)
		Signature half0 = new Signature(split(componentTypes, 2, 0))
		Signature half1 = new Signature(split(componentTypes, 2, 1))

		expect:
		signature.masks(half0)
		signature.masks(half1)

		!half0.masks(signature)
		!half1.masks(signature)
	}

	def "masks all added component types"() {
		List<Class<? extends Component>> addedTypes = []

		expect:
		!signature.masks(new Signature(type))

		when:
		signature.add(type)
		addedTypes.push(type)

		then:
		addedTypes.each { signature.masks(new Signature(it)) }
		signature.masks(new Signature(addedTypes))

		where:
		type << componentTypes
	}
	def "adding present component type changes nothing"() {
		when:
		signature.add(type)
		then:
		signature.masks(new Signature(type))

		when:
		signature.add(type)
		then:
		signature.masks(new Signature(type))

		where:
		type << componentTypes
	}

	def "does not mask removed component type"() {
		when:
		signature.add(type)
		then:
		signature.masks(new Signature(type))

		when:
		signature.remove(type)
		then:
		!signature.masks(new Signature(type))

		where:
		type << componentTypes
	}
	def "removing missing component type changes nothing"() {
		when:
		signature.remove(type)
		then:
		!signature.masks(new Signature(type))

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

	private static List<Class<? extends Component>> split(List<Class<? extends Component>> list, int partitions, int partition) {
		return list.collate(list.size() / partitions as int)[partition]
	}
}
