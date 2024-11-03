package dev.kkorolyov.pancake.core.component.limit

import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Specification

class TransformLimitSpec extends Specification {
	TransformLimit limit = new TransformLimit()

	def "auto-updates translationMin"() {
		when:
		limit.translationMax.set(Vector3.of(-1, 0, 2))
		then:
		limit.translationMin == Vector3.of(-1, 0, 0)
	}
	def "auto-updates translationMax"() {
		when:
		limit.translationMin.set(Vector3.of(-1, 0, 2))
		then:
		limit.translationMax == Vector3.of(0, 0, 2)
	}
	def "auto-updates scaleMin"() {
		when:
		limit.scaleMax.set(Vector3.of(-1, 0, 2))
		then:
		limit.scaleMin == Vector3.of(-1, 0, 1)
	}
	def "auto-updates scaleMax"() {
		when:
		limit.scaleMin.set(Vector3.of(-1, 0, 2))
		then:
		limit.scaleMax == Vector3.of(1, 1, 2)
	}

	def "constrains lesser values"() {
		Transform transform = new Transform().with {
			it.translation.set(translation)
			it.scale.set(scale)
			it
		}
		limit.translationMin.set(minTranslation)
		limit.translationMax.set(Vector3.of(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE))
		limit.scaleMin.set(minScale)
		limit.scaleMax.set(Vector3.of(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE))

		when:
		limit.limit(transform)

		then:
		transform.translation == expectedTranslation
		transform.scale == expectedScale

		where:
		translation << [Vector3.of(), Vector3.of(1, 2, 3)]
		scale << [Vector3.of(1, 1, 1), Vector3.of(2, 5, 10)]
		minTranslation << [Vector3.of(1, 1, 1), Vector3.of(1, 1, 1)]
		minScale << [Vector3.of(2, 2, 2), Vector3.of(2, 2, 2)]
		expectedTranslation << [Vector3.of(1, 1, 1), Vector3.of(1, 2, 3)]
		expectedScale << [Vector3.of(2, 2, 2), Vector3.of(2, 5, 10)]
	}
	def "constrains greater values"() {
		Transform transform = new Transform().with {
			it.translation.set(translation)
			it.scale.set(scale)
			it
		}
		limit.translationMin.set(Vector3.of(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE))
		limit.translationMax.set(maxTranslation)
		limit.scaleMin.set(Vector3.of(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE))
		limit.scaleMax.set(maxScale)

		when:
		limit.limit(transform)

		then:
		transform.translation == expectedTranslation
		transform.scale == expectedScale

		where:
		translation << [Vector3.of(), Vector3.of(1, 2, 3)]
		scale << [Vector3.of(1, 1, 1), Vector3.of(2, 5, 10)]
		maxTranslation << [Vector3.of(1, 1, 1), Vector3.of(1, 1, 1)]
		maxScale << [Vector3.of(2, 2, 2), Vector3.of(2, 2, 2)]
		expectedTranslation << [Vector3.of(0, 0, 0), Vector3.of(1, 1, 1)]
		expectedScale << [Vector3.of(1, 1, 1), Vector3.of(2, 2, 2)]
	}
}
