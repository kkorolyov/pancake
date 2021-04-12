package dev.kkorolyov.pancake.core.component.movement

import dev.kkorolyov.pancake.platform.math.Vector3
import dev.kkorolyov.pancake.platform.math.Vectors

import spock.lang.Specification

class ForceSpec extends Specification {
	def "accelerates"() {
		Vector3 velocity = Vectors.create(1, 1, 1)
		Force force = new Force(Vectors.create(2, 2, 2))

		when:
		force.accelerate(velocity, 2, 4)

		then:
		velocity == Vectors.create(5, 5, 5)
	}
}
