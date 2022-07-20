package dev.kkorolyov.pancake.core.component.movement

import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Specification

class ForceSpec extends Specification {
	def "accelerates"() {
		Vector3 velocity = Vector3.of(1, 1, 1)
		Force force = new Force(Vector3.of(2, 2, 2))

		when:
		force.accelerate(velocity, 2, 4)

		then:
		velocity == Vector3.of(5, 5, 5)
	}
}
