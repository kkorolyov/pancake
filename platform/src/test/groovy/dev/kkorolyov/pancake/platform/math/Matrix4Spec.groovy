package dev.kkorolyov.pancake.platform.math

import spock.lang.Specification

class Matrix4Spec extends Specification {
	def "computes determinant"() {
		expect:
		Matrix4.determinant(matrix) == determinant

		where:
		matrix << [
				Matrix4.of(),
				Matrix4.of(
						1, 0, 0, 0,
						4, 5, 3, 2,
						8, 0, 6, 4,
						0, 0, 3, 4
				)
		]
		determinant << [1, 60]
	}

	def "scales by scalar"() {
		when:
		matrix.scale(value)

		then:
		matrix == expected

		where:
		matrix << [
				Matrix4.of(
						1, 2, 3, 4,
						4, 3, 2, 1,
						1, 3, 5, 7,
						7, 5, 3, 1
				)
		]
		value << [3]
		expected << [
				Matrix4.of(
						3, 6, 9, 12,
						12, 9, 6, 3,
						3, 9, 15, 21,
						21, 15, 9, 3
				)
		]
	}

	def "adds"() {
		when:
		matrix.add(other)

		then:
		matrix == expected

		where:
		matrix << [
				Matrix4.of(
						1, 2, 3, 4,
						4, 3, 2, 1,
						1, 3, 5, 7,
						7, 5, 3, 1
				)
		]
		other << [
				Matrix4.of(
						1, 1, 1, 1,
						2, 2, 2, 2,
						1, 1, 1, 1,
						3, 3, 3, 3
				)
		]
		expected << [
				Matrix4.of(
						2, 3, 4, 5,
						6, 5, 4, 3,
						2, 4, 6, 8,
						10, 8, 6, 4
				)
		]
	}
	def "adds scaled"() {
		when:
		matrix.add(other, value)

		then:
		matrix == expected

		where:
		matrix << [
				Matrix4.of(
						1, 2, 3, 4,
						4, 3, 2, 1,
						1, 3, 5, 7,
						7, 5, 3, 1
				)
		]
		other << [
				Matrix4.of(
						1, 1, 1, 1,
						2, 2, 2, 2,
						1, 1, 1, 1,
						3, 3, 3, 3
				)
		]
		value << [3]
		expected << [
				Matrix4.of(
						4, 5, 6, 7,
						10, 9, 8, 7,
						4, 6, 8, 10,
						16, 14, 12, 10
				)
		]
	}

	def "multiplies"() {
		when:
		matrix.multiply(other)

		then:
		matrix == expected

		where:
		matrix << [
				Matrix4.of(),
				Matrix4.of(
						1, 2, 3, 4,
						4, 3, 2, 1,
						1, 3, 5, 7,
						7, 5, 3, 1
				)
		]
		other << [
				Matrix4.of(),
				Matrix4.of(
						1, 1, 1, 1,
						2, 2, 2, 2,
						1, 1, 1, 1,
						3, 3, 3, 3
				)
		]
		expected << [
				Matrix4.of(),
				Matrix4.of(
						20, 20, 20, 20,
						15, 15, 15, 15,
						33, 33, 33, 33,
						23, 23, 23, 23
				)
		]
	}
	def "multiplies to"() {
		when:
		other.multiplyTo(matrix)

		then:
		other == expected

		where:
		matrix << [
				Matrix4.of(),
				Matrix4.of(
						1, 2, 3, 4,
						4, 3, 2, 1,
						1, 3, 5, 7,
						7, 5, 3, 1
				)
		]
		other << [
				Matrix4.of(),
				Matrix4.of(
						1, 1, 1, 1,
						2, 2, 2, 2,
						1, 1, 1, 1,
						3, 3, 3, 3
				)
		]
		expected << [
				Matrix4.of(),
				Matrix4.of(
						20, 20, 20, 20,
						15, 15, 15, 15,
						33, 33, 33, 33,
						23, 23, 23, 23
				)
		]
	}

	def "translates"() {
		when:
		matrix.translate(translation)

		then:
		matrix == expected

		where:
		matrix << [
				Matrix4.of()
		]
		translation << [
				Vector3.of(4, 5, 10)
		]
		expected << [
				Matrix4.of(
						1, 0, 0, 4,
						0, 1, 0, 5,
						0, 0, 1, 10,
						0, 0, 0, 1
				)
		]
	}
	def "rotates"() {
		when:
		matrix.rotate(radians, axis)

		then:
		matrix == expected

		where:
		matrix << [
				Matrix4.of(),
				Matrix4.of(),
				Matrix4.of(),
				Matrix4.of()
		]
		radians << [
				Math.PI * 2,
				Math.PI / 2,
				Math.PI,
				Math.PI * 3 / 2,
		]
		axis << [
				Vector3.of(1, 0, 0),
				Vector3.of(1, 0, 0),
				Vector3.of(0, 1, 0),
				Vector3.of(0, 0, 1)
		]
		expected << [
				Matrix4.of(),
				Matrix4.of(
						1, 0, 0, 0,
						0, Math.cos(Math.PI / 2), -Math.sin(Math.PI / 2), 0,
						0, Math.sin(Math.PI / 2), Math.cos(Math.PI / 2), 0,
						0, 0, 0, 1
				),
				Matrix4.of(
						-1, 0, 0, 0,
						0, 1, 0, 0,
						0, 0, -1, 0,
						0, 0, 0, 1
				),
				Matrix4.of(
						Math.cos(Math.PI * 3 / 2), -Math.sin(Math.PI * 3 / 2), 0, 0,
						Math.sin(Math.PI * 3 / 2), Math.cos(Math.PI * 3 / 2), 0, 0,
						0, 0, 1, 0,
						0, 0, 0, 1
				)
		]
	}
	def "scales"() {
		when:
		matrix.scale(scale)

		then:
		matrix == expected

		where:
		matrix << [
				Matrix4.of()
		]
		scale << [
				Vector3.of(4, 5, 10)
		]
		expected << [
				Matrix4.of(
						4, 0, 0, 0,
						0, 5, 0, 0,
						0, 0, 10, 0,
						0, 0, 0, 1
				)
		]
	}
}
