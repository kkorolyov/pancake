package dev.kkorolyov.pancake.platform.math

import spock.lang.Specification

class Vector3Spec extends Specification {
	def "computes magnitude"() {
		expect:
		Vector3.magnitude(vector) == magnitude

		where:
		vector << [Vectors.create(0, 0, 0), Vectors.create(0, 0, 1), Vectors.create(0, 1, 0), Vectors.create(1, 0, 0), Vectors.create(1, 1, 1)]
		magnitude << [0, 1, 1, 1, Math.sqrt(3)]
	}

	def "computes dot"() {
		expect:
		Vector3.dot(vector, other) == dot

		where:
		vector << [Vectors.create(0, 0, 0), Vectors.create(0, 0, 1), Vectors.create(0, 1, 0), Vectors.create(1, 0, 0), Vectors.create(1, 1, 1)]
		other = vector
		dot << [0, 1, 1, 1, 3]
	}

	def "computes distance"() {
		expect:
		Vector3.distance(vector, other) == distance

		where:
		vector << [Vectors.create(0, 0, 0), Vectors.create(0, 0, 0), Vectors.create(0, 0, 0), Vectors.create(0, 0, 0), Vectors.create(1, 1, 1)]
		other << [Vectors.create(0, 0, 0), Vectors.create(0, 0, 1), Vectors.create(0, 1, 0), Vectors.create(1, 0, 0), Vectors.create(2, 2, 2)]
		distance << [0, 1, 1, 1, Math.sqrt(3)]
	}

	def "normalizes"() {
		when:
		vector.normalize()

		then:
		vector == unit

		where:
		vector << [Vectors.create(0, 0, 0), Vectors.create(0, 0, 4), Vectors.create(0, 5, 0), Vectors.create(6, 0, 0), Vectors.create(7, 7, 7)]
		unit << [Vectors.create(0, 0, 0), Vectors.create(0, 0, 1), Vectors.create(0, 1, 0), Vectors.create(1, 0, 0), Vectors.create(7 / Math.sqrt(147), 7 / Math.sqrt(147), 7 / Math.sqrt(147))]
	}

	def "projects"() {
		when:
		vector.project(other)

		then:
		vector == projection

		where:
		vector << [Vectors.create(1, 0, 0), Vectors.create(1, 1, 0), Vectors.create(2, 2, 0), Vectors.create(4, 1, 0)]
		other << [Vectors.create(1, 0, 0), Vectors.create(1, 0, 0), Vectors.create(0, 1, 0), Vectors.create(1, 0, 0)]
		projection << [Vectors.create(1, 0, 0), Vectors.create(1, 0, 0), Vectors.create(0, 2, 0), Vectors.create(4, 0, 0)]
	}
}
