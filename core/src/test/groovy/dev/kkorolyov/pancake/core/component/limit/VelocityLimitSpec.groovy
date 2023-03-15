package dev.kkorolyov.pancake.core.component.limit

import dev.kkorolyov.pancake.core.component.movement.Velocity
import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Specification

class VelocityLimitSpec extends Specification {
	def "constrains greater values"() {
		Velocity velocity = new Velocity(velocityV)
		VelocityLimit limit = new VelocityLimit(limitV)

		when:
		limit.limit(velocity)

		then:
		velocity.value == expected

		where:
		velocityV << [Vector3.of(1, 1, 1), Vector3.of(5)]
		expected << [Vector3.of(1 / Math.sqrt(3), 1 / Math.sqrt(3), 1 / Math.sqrt(3)), Vector3.of(4)]
		limitV << [1, 4]
	}

	def "leaves lesser values"() {
		Velocity velocity = new Velocity(velocityV)
		VelocityLimit limit = new VelocityLimit(limitV)

		when:
		limit.limit(velocity)

		then:
		velocity.value == velocityV

		where:
		velocityV << [Vector3.of(1, 1, 1), Vector3.of(10, 1, 1)]
		limitV << [2, 15]
	}
}
