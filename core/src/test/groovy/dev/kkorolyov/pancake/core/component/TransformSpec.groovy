package dev.kkorolyov.pancake.core.component

import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Specification

class TransformSpec extends Specification {
	Transform transform = new Transform()

	def "transforms with translation"() {
		Vector3 vector = Vector3.of()

		when:
		transform.translation.add(translation)
		vector.transform(transform.matrix)

		then:
		vector == translation

		where:
		translation << [Vector3.of(), Vector3.of(1, 0, 0), Vector3.of(1, 1, 1), Vector3.of(4, 7, -3)]
	}
	def "transforms with rotation"() {
		Vector3 vector = Vector3.of(1, 0, 0)

		when:
		transform.rotation.rotate(rads, axis)
		vector.transform(transform.matrix)

		then:
		vector == expected

		where:
		rads << [Math.PI / 2, Math.PI, Math.PI * 2]
		axis << [Vector3.of(0, 0, 1), Vector3.of(0, 0, 1), Vector3.of(0, 0, 1)]
		expected << [Vector3.of(0, 1, 0), Vector3.of(-1, 0, 0), Vector3.of(1, 0, 0)]
	}
	def "transforms with scaling"() {
		Vector3 vector = Vector3.of(1, 1, 1)

		when:
		transform.scale.set(scale)
		vector.transform(transform.matrix)

		then:
		vector == scale

		where:
		scale << [Vector3.of(), Vector3.of(1, 1, 1), Vector3.of(1, 2, 3), Vector3.of(4, 6, -8)]
	}
	def "transforms with translation, rotation, scaling"() {
		Vector3 vector = Vector3.of()

		when:
		transform.translation.add(translation)
		transform.rotation.rotate(rads, axis)
		transform.scale.set(scale)

		vector.transform(transform.matrix)

		then:
		vector == expected

		where:
		translation         | rads        | axis                | scale               | expected
		Vector3.of(1, 2, 3) | Math.PI / 2 | Vector3.of(0, 0, 1) | Vector3.of()        | Vector3.of(1, 2, 3)
		Vector3.of(1, 1, 1) | Math.PI / 2 | Vector3.of(0, 0, 1) | Vector3.of(2, 2, 2) | Vector3.of(1, 1, 1)
	}

	def "transforms with relative translation"() {
		Vector3 vector = Vector3.of()
		transform = new Transform().with {
			it.parent = new Transform().with {
				it.translation.set(parentTranslation)
				it
			}
			it
		}

		when:
		transform.translation.set(translation)
		vector.transform(transform.matrix)

		then:
		vector == expected

		where:
		parentTranslation    | translation         | expected
		Vector3.of()         | Vector3.of(1, 1, 1) | Vector3.of(1, 1, 1)
		Vector3.of(1, 1, 1)  | Vector3.of()        | Vector3.of(1, 1, 1)
		Vector3.of(-4, 1, 6) | Vector3.of(8, 4, 0) | Vector3.of(4, 5, 6)
	}

	def "transforms with relative rotation"() {
		Vector3 vector = Vector3.of(1, 0, 0)
		transform = new Transform().with {
			it.parent = new Transform().with {
				it.rotation.rotate(parentRads, axis)
				it
			}
			it
		}

		when:
		transform.rotation.rotate(rads, axis)
		vector.transform(transform.matrix)

		then:
		vector == expected

		where:
		parentRads  | rads        | axis                | expected
		Math.PI / 2 | Math.PI / 2 | Vector3.of(0, 0, 1) | Vector3.of(-1, 0, 0)
		Math.PI / 2 | Math.PI     | Vector3.of(0, 0, 1) | Vector3.of(0, -1, 0)
		Math.PI     | Math.PI * 2 | Vector3.of(0, 0, 1) | Vector3.of(-1, 0, 0)
	}
	def "transforms with relative scaling"() {
		Vector3 vector = Vector3.of(1, 1, 1)
		transform = new Transform().with {
			it.parent = new Transform().with {
				it.scale.set(parentScale)
				it
			}
			it
		}

		when:
		transform.scale.set(scale)
		vector.transform(transform.matrix)

		then:
		vector == expected

		where:
		parentScale          | scale               | expected
		Vector3.of()         | Vector3.of(1, 1, 1) | Vector3.of()
		Vector3.of(1, 1, 1)  | Vector3.of()        | Vector3.of(0)
		Vector3.of(-4, 1, 6) | Vector3.of(8, 4, 0) | Vector3.of(-32, 4, 0)
	}
	def "transforms with relative translation, rotation, scaling"() {
		Vector3 vector = Vector3.of()
		transform = new Transform().with {
			it.parent = new Transform().with {
				it.translation.set(parentTranslation)
				it.rotation.rotate(parentRads, axis)
				it.scale.set(parentScale)
				it
			}
			it
		}

		when:
		transform.translation.add(translation)
		transform.rotation.rotate(rads, axis)
		transform.scale.set(scale)

		vector.transform(transform.matrix)

		then:
		vector == expected

		where:
		parentTranslation   | translation         | parentRads | rads        | axis                | parentScale         | scale               | expected
		Vector3.of(1, 1, 1) | Vector3.of(1, 2, 3) | Math.PI    | Math.PI / 2 | Vector3.of(0, 0, 1) | Vector3.of(2, 2, 2) | Vector3.of()        | Vector3.of(-1, -3, 7)
		Vector3.of(1, 1, 1) | Vector3.of(1, 1, 1) | Math.PI    | Math.PI / 2 | Vector3.of(0, 0, 1) | Vector3.of(2, 2, 2) | Vector3.of(2, 2, 2) | Vector3.of(-1, -1, 3)
	}
}
