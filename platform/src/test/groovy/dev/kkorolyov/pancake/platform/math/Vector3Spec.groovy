package dev.kkorolyov.pancake.platform.math

import spock.lang.Specification

class Vector3Spec extends Specification {
	def "computes magnitude"() {
		expect:
		Vector3.magnitude(vector) == magnitude

		where:
		vector << [Vector3.of(0, 0, 0), Vector3.of(0, 0, 1), Vector3.of(0, 1, 0), Vector3.of(1, 0, 0), Vector3.of(1, 1, 1)]
		magnitude << [0, 1, 1, 1, Math.sqrt(3)]
	}
	def "computes dot"() {
		expect:
		Vector3.dot(vector, other) == dot

		where:
		vector << [Vector3.of(0, 0, 0), Vector3.of(0, 0, 1), Vector3.of(0, 1, 0), Vector3.of(1, 0, 0), Vector3.of(1, 1, 1)]
		other = vector
		dot << [0, 1, 1, 1, 3]
	}
	def "computes distance"() {
		expect:
		Vector3.distance(vector, other) == distance

		where:
		vector << [Vector3.of(0, 0, 0), Vector3.of(0, 0, 0), Vector3.of(0, 0, 0), Vector3.of(0, 0, 0), Vector3.of(1, 1, 1)]
		other << [Vector3.of(0, 0, 0), Vector3.of(0, 0, 1), Vector3.of(0, 1, 0), Vector3.of(1, 0, 0), Vector3.of(2, 2, 2)]
		distance << [0, 1, 1, 1, Math.sqrt(3)]
	}

	def "normalizes"() {
		when:
		vector.normalize()

		then:
		vector == expected

		where:
		vector << [Vector3.of(0, 0, 0), Vector3.of(0, 0, 4), Vector3.of(0, 5, 0), Vector3.of(6, 0, 0), Vector3.of(7, 7, 7)]
		expected << [Vector3.of(0, 0, 0), Vector3.of(0, 0, 1), Vector3.of(0, 1, 0), Vector3.of(1, 0, 0), Vector3.of(7 / Math.sqrt(147), 7 / Math.sqrt(147), 7 / Math.sqrt(147))]
	}
	def "orthagonalizes"() {
		when:
		vector.orthogonal()

		then:
		vector == expected

		where:
		vector << [Vector3.of(0, 0), Vector3.of(1, 0), Vector3.of(0, 1), Vector3.of(1, 1)]
		expected << [Vector3.of(0, 0), Vector3.of(0, 1), Vector3.of(-1, 0), Vector3.of(-1, 1)]
	}
	def "projects"() {
		when:
		vector.project(other)

		then:
		vector == expected

		where:
		vector << [Vector3.of(1, 0, 0), Vector3.of(1, 1, 0), Vector3.of(2, 2, 0), Vector3.of(4, 1, 0)]
		other << [Vector3.of(1, 0, 0), Vector3.of(1, 0, 0), Vector3.of(0, 1, 0), Vector3.of(1, 0, 0)]
		expected << [Vector3.of(1, 0, 0), Vector3.of(1, 0, 0), Vector3.of(0, 2, 0), Vector3.of(4, 0, 0)]
	}
	def "reflects"() {
		when:
		vector.reflect(other)

		then:
		vector == expected

		where:
		vector << [Vector3.of(1, 1), Vector3.of(1, 1), Vector3.of(1, 0), Vector3.of(0, 1)]
		other << [Vector3.of(0, 1), Vector3.of(1, 0), Vector3.of(1, 1), Vector3.of(1, 1)]
		expected << [Vector3.of(1, -1), Vector3.of(-1, 1), Vector3.of(0, -1), Vector3.of(-1, 0)]
	}

	def "scales"() {
		when:
		vector.scale(value)

		then:
		vector == expected

		where:
		vector << [Vector3.of(1, 2, 3), Vector3.of(1, 2, 3), Vector3.of(3, -3, -1)]
		value << [0, 2, -3]
		expected << [Vector3.of(), Vector3.of(2, 4, 6), Vector3.of(-9, 9, 3)]
	}

	def "adds"() {
		when:
		vector.add(other)

		then:
		vector == expected

		where:
		vector << [Vector3.of(), Vector3.of(1, 2, 3), Vector3.of(4, 2, 1)]
		other << [Vector3.of(1, 1, 2), Vector3.of(2, 1, 4), Vector3.of(-4, -3, 1)]
		expected << [Vector3.of(1, 1, 2), Vector3.of(3, 3, 7), Vector3.of(0, -1, 2)]
	}
	def "adds scaled"() {
		when:
		vector.add(other, scale)

		then:
		vector == expected

		where:
		vector << [Vector3.of(), Vector3.of(1, 2, 3), Vector3.of(4, 2, 1)]
		other << [Vector3.of(1, 1, 2), Vector3.of(2, 1, 4), Vector3.of(4, -3, 1)]
		scale << [0, 2, -3]
		expected << [Vector3.of(), Vector3.of(5, 4, 11), Vector3.of(-8, 11, -2)]
	}
}
