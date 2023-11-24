package dev.kkorolyov.pancake.platform.io

import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Specification

class ObjectConvertersSpec extends Specification {
	def "reads vector2 with 1 element"() {
		expect:
		ObjectConverters.vector2().convertOut([x] as Object) == Vector2.of(x)

		where:
		x << (1..4)
	}
	def "reads vector2 with 2 elements"() {
		expect:
		ObjectConverters.vector2().convertOut([x, y] as Object) == Vector2.of(x, y)

		where:
		x << (1..4)
		y << (4..1)
	}

	def "reads vector3 with 1 element"() {
		expect:
		ObjectConverters.vector3().convertOut([x] as Object) == Vector3.of(x)

		where:
		x << (1..4)
	}
	def "reads vector3 with 2 elements"() {
		expect:
		ObjectConverters.vector3().convertOut([x, y] as Object) == Vector3.of(x, y)

		where:
		x << (1..4)
		y << (4..1)
	}
	def "reads vector3 with 3 elements"() {
		expect:
		ObjectConverters.vector3().convertOut([x, y, z] as Object) == Vector3.of(x, y, z)

		where:
		x << (1..4)
		y << (4..1)
		z << (3..6)
	}
}
