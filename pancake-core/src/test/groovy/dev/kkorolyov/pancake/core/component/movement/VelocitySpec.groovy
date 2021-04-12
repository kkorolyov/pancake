package dev.kkorolyov.pancake.core.component.movement

import dev.kkorolyov.pancake.platform.math.Vector3
import dev.kkorolyov.pancake.platform.math.Vectors

import spock.lang.Specification

class VelocitySpec extends Specification {
	def "moves"() {
		Vector3 position = Vectors.create(1, 1, 1)
		Velocity velocity = new Velocity(Vectors.create(2, 2, 2))

		when:
		velocity.move(position, 3)

		then:
		position == Vectors.create(7, 7, 7)
	}
}
