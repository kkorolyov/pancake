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

	def "normalizes"() {
		when:
		vector.normalize()

		then:
		vector == unit

		where:
		vector << [Vector2.of(0, 0), Vector2.of(0, 4), Vector2.of(5, 0), Vector2.of(7, 7)]
		unit << [Vector2.of(0, 0), Vector2.of(0, 1), Vector2.of(1, 0), Vector2.of(7 / Math.sqrt(98), 7 / Math.sqrt(98))]
	}

	def "projects"() {
		when:
		vector.project(other)

		then:
		vector == projection

		where:
		vector << [Vector2.of(1, 0), Vector2.of(1, 1), Vector2.of(2, 2), Vector2.of(4, 1)]
		other << [Vector2.of(1, 0), Vector2.of(1, 0), Vector2.of(0, 1), Vector2.of(1, 0)]
		projection << [Vector2.of(1, 0), Vector2.of(1, 0), Vector2.of(0, 2), Vector2.of(4, 0)]
	}
}
