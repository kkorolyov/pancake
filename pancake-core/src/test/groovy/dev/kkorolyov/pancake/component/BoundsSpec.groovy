package dev.kkorolyov.pancake.component

import dev.kkorolyov.pancake.math.Vector

import spock.lang.Shared
import spock.lang.Specification

import static dev.kkorolyov.pancake.component.Bounds.*

class BoundsSpec extends Specification {
	@Shared Vector box = new Vector()
	@Shared float radius = 1

	Bounds bounds = new Bounds(box, radius)

	def "complains if both box and radius undefined"() {
		when:
		new Bounds(null, null)
		then:
		thrown(IllegalStateException)

		when:
		new Bounds(box)
				.setBox(null)
		then:
		thrown(IllegalStateException)

		when:
		new Bounds(radius)
				.setRadius(null)
		then:
		thrown(IllegalStateException)

		when:
		new Bounds(box, radius)
				.setBox(null)
				.setRadius(null)
		then:
		thrown(IllegalStateException)
	}

	def "returns appropriate intersection type"() {
		Bounds box = new Bounds(box)
		Bounds sphere = new Bounds(radius)

		expect:
		bounds.getIntersectionType(bounds) == SPHERE_SPHERE
		bounds.getIntersectionType(box) == BOX_BOX
		bounds.getIntersectionType(sphere) == SPHERE_SPHERE
		box.getIntersectionType(sphere) == BOX_SPHERE
		sphere.getIntersectionType(box) == SPHERE_BOX
	}

	def "has box if box != null"() {
		when:
		bounds.setBox(null)
		then:
		!bounds.hasBox()

		when:
		bounds.setBox(box)
		then:
		bounds.hasBox()
	}
	def "has radius if radius != null and > 0"() {
		when:
		bounds.setRadius(null)
		then:
		!bounds.hasRadius()

		when:
		bounds.setRadius(0)
		then:
		!bounds.hasRadius()

		when:
		bounds.setRadius(radius)
		then:
		bounds.hasRadius()
	}
}
