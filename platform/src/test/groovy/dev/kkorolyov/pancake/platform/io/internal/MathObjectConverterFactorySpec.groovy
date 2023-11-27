package dev.kkorolyov.pancake.platform.io.internal

import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Shared
import spock.lang.Specification

class MathObjectConverterFactorySpec extends Specification {
	@Shared
	def converterFactory = new MathObjectConverterFactory()

	def vector2Converter = converterFactory.get(Iterable, Vector2)
	def vector3Converter = converterFactory.get(Iterable, Vector3)

	def "reads vector2 with 1 element"() {
		expect:
		vector2Converter.convertOut([x] as Object) == Vector2.of(x)

		where:
		x << (1..4)
	}
	def "reads vector2 with 2 elements"() {
		expect:
		vector2Converter.convertOut([x, y] as Object) == Vector2.of(x, y)

		where:
		x << (1..4)
		y << (4..1)
	}
	def "reads written vector2"() {
		Object obj = [x, y]

		expect:
		vector2Converter.convertIn(vector2Converter.convertOut(obj as Object)) == obj

		where:
		x << (1..4)
		y << (4..1)
	}

	def "reads vector3 with 1 element"() {
		expect:
		vector3Converter.convertOut([x] as Object) == Vector3.of(x)

		where:
		x << (1..4)
	}
	def "reads vector3 with 2 elements"() {
		expect:
		vector3Converter.convertOut([x, y] as Object) == Vector3.of(x, y)

		where:
		x << (1..4)
		y << (4..1)
	}
	def "reads vector3 with 3 elements"() {
		expect:
		vector3Converter.convertOut([x, y, z] as Object) == Vector3.of(x, y, z)

		where:
		x << (1..4)
		y << (4..1)
		z << (3..6)
	}
	def "reads written vector3"() {
		Object obj = [x, y, z]

		expect:
		vector3Converter.convertIn(vector3Converter.convertOut(obj as Object)) == obj

		where:
		x << (1..4)
		y << (4..1)
		z << (3..6)
	}
}
