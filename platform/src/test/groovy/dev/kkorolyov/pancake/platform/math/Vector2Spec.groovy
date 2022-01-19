package dev.kkorolyov.pancake.platform.math


import spock.lang.Specification

class Vector2Spec extends Specification {
	def "computes magnitude"() {
		expect:
		Vector2.magnitude(vector) == magnitude

		where:
		vector << [Vectors.create(0, 0), Vectors.create(0, 1), Vectors.create(1, 0), Vectors.create(1, 1)]
		magnitude << [0, 1, 1, Math.sqrt(2)]
	}

	def "computes dot"() {
		expect:
		Vector2.dot(vector, other) == dot

		where:
		vector << [Vectors.create(0, 0), Vectors.create(0, 1), Vectors.create(1, 0), Vectors.create(1, 1)]
		other = vector
		dot << [0, 1, 1, 2]
	}

	def "computes distance"() {
		expect:
		Vector2.distance(vector, other) == distance

		where:
		vector << [Vectors.create(0, 0), Vectors.create(0, 0), Vectors.create(0, 0), Vectors.create(1, 1)]
		other << [Vectors.create(0, 0), Vectors.create(0, 1), Vectors.create(1, 0), Vectors.create(2, 2)]
		distance << [0, 1, 1, Math.sqrt(2)]
	}

	def "normalizes"() {
		when:
		vector.normalize()

		then:
		vector == unit

		where:
		vector << [Vectors.create(0, 0), Vectors.create(0, 4), Vectors.create(5, 0), Vectors.create(7, 7)]
		unit << [Vectors.create(0, 0), Vectors.create(0, 1), Vectors.create(1, 0), Vectors.create(7 / Math.sqrt(98), 7 / Math.sqrt(98))]
	}

	def "projects"() {
		when:
		vector.project(other)

		then:
		vector == projection

		where:
		vector << [Vectors.create(1, 0), Vectors.create(1, 1), Vectors.create(2, 2), Vectors.create(4, 1)]
		other << [Vectors.create(1, 0), Vectors.create(1, 0), Vectors.create(0, 1), Vectors.create(1, 0)]
		projection << [Vectors.create(1, 0), Vectors.create(1, 0), Vectors.create(0, 2), Vectors.create(4, 0)]
	}
}
