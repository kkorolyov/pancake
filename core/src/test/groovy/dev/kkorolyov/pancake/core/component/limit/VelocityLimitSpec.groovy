package dev.kkorolyov.pancake.core.component.limit

import dev.kkorolyov.pancake.core.component.Velocity
import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Specification

class VelocityLimitSpec extends Specification {
	def "constrains greater values"() {
		Velocity velocity = new Velocity(linearV, angularV)
		VelocityLimit limit = new VelocityLimit(limitLinearV, limitAngularV)

		when:
		limit.limit(velocity)

		then:
		velocity.linear == expectedLinear
		velocity.angular == expectedLinear

		where:
		linearV << [Vector3.of(1, 1, 1), Vector3.of(5)]
		angularV << [Vector3.of(1, 1, 1), Vector3.of(5)]
		expectedLinear << [Vector3.of(1 / Math.sqrt(3), 1 / Math.sqrt(3), 1 / Math.sqrt(3)), Vector3.of(4)]
		expectedAngular << [Vector3.of(1 / Math.sqrt(3), 1 / Math.sqrt(3), 1 / Math.sqrt(3)), Vector3.of(4)]
		limitLinearV << [1, 4]
		limitAngularV << [1, 4]
	}

	def "leaves lesser values"() {
		Velocity velocity = new Velocity(linearV, angularV)
		VelocityLimit limit = new VelocityLimit(limitLinearV, limitAngularV)

		when:
		limit.limit(velocity)

		then:
		velocity.linear == linearV
		velocity.angular == angularV

		where:
		linearV << [Vector3.of(1, 1, 1), Vector3.of(10, 1, 1)]
		angularV << [Vector3.of(1, 1, 1), Vector3.of(10, 1, 1)]
		limitLinearV << [2, 15]
		limitAngularV << [2, 15]
	}
}
