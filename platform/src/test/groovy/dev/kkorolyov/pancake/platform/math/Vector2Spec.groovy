package dev.kkorolyov.pancake.platform.math

import spock.lang.Specification

class Vector2Spec extends Specification {
	def "computes magnitude"() {
		expect:
		Vector2.magnitude(vector) == magnitude

		where:
		vector << [Vector2.of(0, 0), Vector2.of(0, 1), Vector2.of(1, 0), Vector2.of(1, 1)]
		magnitude << [0, 1, 1, Math.sqrt(2)]
	}
	def "computes dot"() {
		expect:
		Vector2.dot(vector, other) == dot

		where:
		vector << [Vector2.of(0, 0), Vector2.of(0, 1), Vector2.of(1, 0), Vector2.of(1, 1)]
		other = vector
		dot << [0, 1, 1, 2]
	}
	def "computes distance"() {
		expect:
		Vector2.distance(vector, other) == distance

		where:
		vector << [Vector2.of(0, 0), Vector2.of(0, 0), Vector2.of(0, 0), Vector2.of(1, 1)]
		other << [Vector2.of(0, 0), Vector2.of(0, 1), Vector2.of(1, 0), Vector2.of(2, 2)]
		distance << [0, 1, 1, Math.sqrt(2)]
	}
	def "computes angle"() {
		expect:
		FloatOps.equals(Vector2.angle(a, b), angle)

		where:
		a                 | b                 | angle
		Vector2.of(0, 0)  | Vector2.of(0, 0)  | 0
		Vector2.of(0, 0)  | Vector2.of(1, 0)  | 0
		Vector2.of(1, 0)  | Vector2.of(0, 1)  | Math.PI / 2
		Vector2.of(1, 0)  | Vector2.of(0, -1) | Math.PI / 2
		Vector2.of(3, 0)  | Vector2.of(0, 2)  | Math.PI / 2
		Vector2.of(-1, 0) | Vector2.of(1, 0)  | Math.PI
		Vector2.of(1, 0)  | Vector2.of(1, 1)  | Math.PI / 4
	}

	def "normalizes"() {
		when:
		vector.normalize()

		then:
		vector == expected

		where:
		vector << [Vector2.of(0, 0), Vector2.of(0, 4), Vector2.of(5, 0), Vector2.of(7, 7)]
		expected << [Vector2.of(0, 0), Vector2.of(0, 1), Vector2.of(1, 0), Vector2.of(7 / Math.sqrt(98), 7 / Math.sqrt(98))]
	}
	def "orthagonalizes"() {
		when:
		vector.orthogonal()

		then:
		vector == expected

		where:
		vector << [Vector2.of(0, 0), Vector2.of(1, 0), Vector2.of(0, 1), Vector2.of(1, 1)]
		expected << [Vector2.of(0, 0), Vector2.of(0, 1), Vector2.of(-1, 0), Vector2.of(-1, 1)]
	}
	def "projects"() {
		when:
		vector.project(other)

		then:
		vector == expected

		where:
		vector << [Vector2.of(1, 0), Vector2.of(1, 1), Vector2.of(2, 2), Vector2.of(4, 1)]
		other << [Vector2.of(1, 0), Vector2.of(1, 0), Vector2.of(0, 1), Vector2.of(1, 0)]
		expected << [Vector2.of(1, 0), Vector2.of(1, 0), Vector2.of(0, 2), Vector2.of(4, 0)]
	}
	def "reflects"() {
		when:
		vector.reflect(other)

		then:
		vector == expected

		where:
		vector << [Vector2.of(1, 1), Vector2.of(1, 1), Vector2.of(1, 0), Vector2.of(0, 1)]
		other << [Vector2.of(0, 1), Vector2.of(1, 0), Vector2.of(1, 1), Vector2.of(1, 1)]
		expected << [Vector2.of(1, -1), Vector2.of(-1, 1), Vector2.of(0, -1), Vector2.of(-1, 0)]
	}

	def "scales"() {
		when:
		vector.scale(value)

		then:
		vector == expected

		where:
		vector << [Vector2.of(1, 2), Vector2.of(1, 2), Vector2.of(3, -3)]
		value << [0, 2, -3]
		expected << [Vector2.of(), Vector2.of(2, 4), Vector2.of(-9, 9)]
	}

	def "adds"() {
		when:
		vector.add(other)

		then:
		vector == expected

		where:
		vector << [Vector2.of(), Vector2.of(1, 2), Vector2.of(4, 2)]
		other << [Vector2.of(1, 1), Vector2.of(2, 1), Vector2.of(-4, -3)]
		expected << [Vector2.of(1, 1), Vector2.of(3, 3), Vector2.of(0, -1)]
	}
	def "adds scaled"() {
		when:
		vector.add(other, scale)

		then:
		vector == expected

		where:
		vector << [Vector2.of(), Vector2.of(1, 2), Vector2.of(4, 2)]
		other << [Vector2.of(1, 1), Vector2.of(2, 1), Vector2.of(4, -3)]
		scale << [0, 2, -3]
		expected << [Vector2.of(), Vector2.of(5, 4), Vector2.of(-8, 11)]
	}
}
