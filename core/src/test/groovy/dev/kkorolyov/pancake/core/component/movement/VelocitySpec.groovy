package dev.kkorolyov.pancake.core.component.movement

import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Specification

class VelocitySpec extends Specification {
	def "moves"() {
		Vector3 position = Vector3.of(1, 1, 1)
		Velocity velocity = new Velocity(Vector3.of(2, 2, 2))

		when:
		velocity.move(position, 3)

		then:
		position == Vector3.of(7, 7, 7)
	}
}
