package dev.kkorolyov.pancake.platform.math

import spock.lang.Specification

import static dev.kkorolyov.pancake.platform.SpecUtilities.randVector

class BoundedVectorStratSpec extends Specification {
	Vector vector = randVector()
	Vector min = new Vector(vector).sub(Vector.all(1000))
	Vector max = new Vector(vector).add(Vector.all(500))

	BoundedVector boundedVector = new BoundedVector(vector, min, max)

	def "bounded by min"() {
		Vector overMin = new Vector(min).scale(4)

		expect:
		boundedVector != min

		when:
		boundedVector.sub(overMin)
		then:
		boundedVector == min
	}
	def "bounded by max"() {
		Vector overMax = new Vector(max).scale(3)

		expect:
		boundedVector != max

		when:
		boundedVector.add(overMax)
		then:
		boundedVector == max
	}

	def "can change within min constraints"() {
		Vector underMin = Vector.sub(boundedVector, min).scale(0.8f)

		when:
		boundedVector.sub(underMin)

		then:
		boundedVector.x > min.x
		boundedVector.y > min.y
		boundedVector.z > min.z
	}
	def "can change within max constraints"() {
		Vector underMax = Vector.sub(max, boundedVector).scale(0.456f)

		when:
		boundedVector.add(underMax)

		then:
		boundedVector.x < max.x
		boundedVector.y < max.y
		boundedVector.z < max.z
	}
}
