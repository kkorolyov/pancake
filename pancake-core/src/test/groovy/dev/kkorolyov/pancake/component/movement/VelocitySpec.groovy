package dev.kkorolyov.pancake.component.movement

import dev.kkorolyov.pancake.math.Vector

import spock.lang.Specification

class VelocitySpec extends Specification {
	Velocity velocity = new Velocity()

	def "returns moved vector"() {
		Vector position = new Vector()

		expect:
		velocity.move(position, dt) == position

		where:
		dt << (1..100)
	}

	def "moves all axes"() {
		velocity.getVelocity().set(velocityComponent, velocityComponent, velocityComponent)
		Vector position = new Vector(positionComponent, positionComponent, positionComponent)
		Vector expectedPosition = new Vector(position)
		expectedPosition.add(velocity.getVelocity(), dt)

		when:
		velocity.move(position, dt)

		then:
		position == expectedPosition

		where:
		positionComponent << (-50..49)
		velocityComponent << (100..1)
		dt << (20..119)
	}
}
