package dev.kkorolyov.pancake

import dev.kkorolyov.pancake.component.*
import spock.lang.Shared
import spock.lang.Specification

import java.lang.reflect.Field

class SignatureSpec extends Specification {
	@Shared List<Class<? extends Component>> componentTypes = [
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
		getSignatureField() == 0;
	}
	private long getSignatureField() {
		Field f = Signature.getDeclaredField("signature")
		f.setAccessible(true)

		return f.get(signature) as long
	}

	def "has all constructor-initialized component types"() {
		when:
		signature = new Signature(componentTypes.toArray(new Class<? extends Component>[componentTypes.size()]))

		then:
		signature.has(componentTypes.toArray(new Class<? extends Component>[componentTypes.size()]))
	}

	def "has added component type"() {
		when:
		signature = new Signature()
		then:
		!signature.has(type)

		when:
		signature.add(type)
		then:
		signature.has(type)

		where:
		type << componentTypes
	}
	def "adding present component type changes nothing"() {
		when:
		signature.add(type)
		then:
		signature.has(type)

		when:
		signature.add(type)
		then:
		signature.has(type)

		where:
		type << componentTypes
	}

	def "does not have removed component type"() {
		when:
		signature.add(type)
		then:
		signature.has(type)

		when:
		signature.remove(type)
		then:
		!signature.has(type)

		where:
		type << componentTypes
	}
	def "removing missing component type changes nothing"() {
		when:
		signature.remove(type)
		then:
		!signature.has(type)

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
		type1 << componentTypes.collate(componentTypes.size() / 2 as int)[0]
		type2 << componentTypes.collate(componentTypes.size() / 2 as int)[1]
	}
}
