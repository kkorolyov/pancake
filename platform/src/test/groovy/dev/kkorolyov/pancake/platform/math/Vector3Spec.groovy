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
		vector == unit

		where:
		vector << [Vector3.of(0, 0, 0), Vector3.of(0, 0, 4), Vector3.of(0, 5, 0), Vector3.of(6, 0, 0), Vector3.of(7, 7, 7)]
		unit << [Vector3.of(0, 0, 0), Vector3.of(0, 0, 1), Vector3.of(0, 1, 0), Vector3.of(1, 0, 0), Vector3.of(7 / Math.sqrt(147), 7 / Math.sqrt(147), 7 / Math.sqrt(147))]
	}

	def "projects"() {
		when:
		vector.project(other)

		then:
		vector == projection

		where:
		vector << [Vector3.of(1, 0, 0), Vector3.of(1, 1, 0), Vector3.of(2, 2, 0), Vector3.of(4, 1, 0)]
		other << [Vector3.of(1, 0, 0), Vector3.of(1, 0, 0), Vector3.of(0, 1, 0), Vector3.of(1, 0, 0)]
		projection << [Vector3.of(1, 0, 0), Vector3.of(1, 0, 0), Vector3.of(0, 2, 0), Vector3.of(4, 0, 0)]
	}
}
