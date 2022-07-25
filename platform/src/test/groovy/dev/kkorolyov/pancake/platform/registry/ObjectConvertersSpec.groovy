package dev.kkorolyov.pancake.platform.registry

import dev.kkorolyov.flub.function.convert.Converter
import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Specification

class ObjectConvertersSpec extends Specification {
	Converter<Object, Vector3> converter = ObjectConverters.vector3()

	def "reads vector3 with 1 element"() {
		expect:
		converter.convert([x] as Object) == Vector3.of(x)

		where:
		x << (1..4)
	}
	def "reads vector3 with 2 elements"() {
		expect:
		converter.convert([x, y] as Object) == Vector3.of(x, y)

		where:
		x << (1..4)
		y << (4..1)
	}
	def "reads vector3 with 3 elements"() {
		expect:
		converter.convert([x, y, z] as Object) == Vector3.of(x, y, z)

		where:
		x << (1..4)
		y << (4..1)
		z << (3..6)
	}
}
