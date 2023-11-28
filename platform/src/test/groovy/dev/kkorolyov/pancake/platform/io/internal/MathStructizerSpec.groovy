package dev.kkorolyov.pancake.platform.io.internal

import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Specification

class MathStructizerSpec extends Specification {
	def structizer = new MathStructizer()

	def "toStructs vector2"() {
		expect:
		structizer.toStruct(Vector2.of(x, y)) == Optional.of([x.doubleValue(), y.doubleValue()])

		where:
		x << (1..4)
		y << (4..1)
	}

	def "fromStructs vector2 with 1 element"() {
		expect:
		structizer.fromStruct(Vector2, [x]) == Optional.of(Vector2.of(x))

		where:
		x << (1..4)
	}
	def "fromStructs vector2 with 2 elements"() {
		expect:
		structizer.fromStruct(Vector2, [x, y]) == Optional.of(Vector2.of(x, y))

		where:
		x << (1..4)
		y << (4..1)
	}

	def "toStructs fromStructed vector2"() {
		Object obj = [x.doubleValue(), y.doubleValue()]

		expect:
		structizer.fromStruct(Vector2, obj).flatMap { structizer.toStruct(it) } == Optional.of(obj)

		where:
		x << (1..4)
		y << (4..1)
	}

	def "toStructs vector3"() {
		expect:
		structizer.toStruct(Vector3.of(x, y, z)) == Optional.of([x.doubleValue(), y.doubleValue(), z.doubleValue()])

		where:
		x << (1..4)
		y << (4..1)
		z << (3..6)
	}

	def "fromStructs vector3 with 1 element"() {
		expect:
		structizer.fromStruct(Vector3, [x]) == Optional.of(Vector3.of(x))

		where:
		x << (1..4)
	}
	def "fromStructs vector3 with 2 elements"() {
		expect:
		structizer.fromStruct(Vector3, [x, y]) == Optional.of(Vector3.of(x, y))

		where:
		x << (1..4)
		y << (4..1)
	}
	def "fromStructs vector3 with 3 elements"() {
		expect:
		structizer.fromStruct(Vector3, [x, y, z]) == Optional.of(Vector3.of(x, y, z))

		where:
		x << (1..4)
		y << (4..1)
		z << (3..6)
	}

	def "toStructs fromStructed vector3"() {
		Object obj = [x.doubleValue(), y.doubleValue(), z.doubleValue()]

		expect:
		structizer.fromStruct(Vector3, obj).flatMap { structizer.toStruct(it) } == Optional.of(obj)

		where:
		x << (1..4)
		y << (4..1)
		z << (3..6)
	}
}
