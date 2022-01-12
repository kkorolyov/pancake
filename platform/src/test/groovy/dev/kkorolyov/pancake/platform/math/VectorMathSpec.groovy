package dev.kkorolyov.pancake.platform.math

import spock.lang.Specification

class VectorMathSpec extends Specification {
	def "computes 2D normal"() {
		expect:
		VectorMath.unit(vector) == normal

		where:
		vector << [Vectors.create(0, 0), Vectors.create(0, 4), Vectors.create(5, 0), Vectors.create(7, 7)]
		normal << [Vectors.create(0, 0), Vectors.create(0, 1), Vectors.create(1, 0), Vectors.create(7 / Math.sqrt(98), 7 / Math.sqrt(98))]
	}
	def "computes 3D normal"() {
		expect:
		VectorMath.unit(vector) == normal

		where:
		vector << [Vectors.create(0, 0, 0), Vectors.create(0, 0, 4), Vectors.create(0, 5, 0), Vectors.create(6, 0, 0), Vectors.create(7, 7, 7)]
		normal << [Vectors.create(0, 0, 0), Vectors.create(0, 0, 1), Vectors.create(0, 1, 0), Vectors.create(1, 0, 0), Vectors.create(7 / Math.sqrt(147), 7 / Math.sqrt(147), 7 / Math.sqrt(147))]
	}

	def "computes 2D magnitude"() {
		expect:
		VectorMath.magnitude(vector) == magnitude

		where:
		vector << [Vectors.create(0, 0), Vectors.create(0, 1), Vectors.create(1, 0), Vectors.create(1, 1)]
		magnitude << [0, 1, 1, Math.sqrt(2)]
	}
	def "computes 3D magnitude"() {
		expect:
		VectorMath.magnitude(vector) == magnitude

		where:
		vector << [Vectors.create(0, 0, 0), Vectors.create(0, 0, 1), Vectors.create(0, 1, 0), Vectors.create(1, 0, 0), Vectors.create(1, 1, 1)]
		magnitude << [0, 1, 1, 1, Math.sqrt(3)]
	}

	def "computes 2D dot"() {
		expect:
		VectorMath.dot(vector, vector1) == dot

		where:
		vector << [Vectors.create(0, 0), Vectors.create(0, 1), Vectors.create(1, 0), Vectors.create(1, 1)]
		vector1 = vector
		dot << [0, 1, 1, 2]
	}
	def "computes 3D dot"() {
		expect:
		VectorMath.dot(vector, vector1) == dot

		where:
		vector << [Vectors.create(0, 0, 0), Vectors.create(0, 0, 1), Vectors.create(0, 1, 0), Vectors.create(1, 0, 0), Vectors.create(1, 1, 1)]
		vector1 = vector
		dot << [0, 1, 1, 1, 3]
	}

	def "computes 2D distance"() {
		expect:
		VectorMath.distance(vector, vector1) == distance

		where:
		vector << [Vectors.create(0, 0), Vectors.create(0, 0), Vectors.create(0, 0), Vectors.create(1, 1)]
		vector1 << [Vectors.create(0, 0), Vectors.create(0, 1), Vectors.create(1, 0), Vectors.create(2, 2)]
		distance << [0, 1, 1, Math.sqrt(2)]
	}
	def "computes 3D distance"() {
		expect:
		VectorMath.distance(vector, vector1) == distance

		where:
		vector << [Vectors.create(0, 0, 0), Vectors.create(0, 0, 0), Vectors.create(0, 0, 0), Vectors.create(0, 0, 0), Vectors.create(1, 1, 1)]
		vector1 << [Vectors.create(0, 0, 0), Vectors.create(0, 0, 1), Vectors.create(0, 1, 0), Vectors.create(1, 0, 0), Vectors.create(2, 2, 2)]
		distance << [0, 1, 1, 1, Math.sqrt(3)]
	}

	def "computes 2D projection"() {
		// TODO
		distance << [0, 1, 1, Math.sqrt(2)]
	}
	def "computes 3D projection"() {
		// TODO
	}
}
