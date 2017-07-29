package dev.kkorolyov.pancake.component.movement

import dev.kkorolyov.pancake.math.Vector

import spock.lang.Shared
import spock.lang.Specification

class VelocitySpec extends Specification {
	@Shared float maxSpeedValue = 15

	Velocity velocity = new Velocity(maxSpeedValue)

	def "returns moved vector"() {
		Vector position = new Vector()

		expect:
		velocity.move(position, dt).is(position)

		where:
		dt << (1..100)
	}
	def "returns capped vector"() {
		expect:
		velocity.cap().is(velocity.getVelocity())
	}

	def "caps all axes"() {
		velocity.getVelocity().set(velocityValue, velocityValue, velocityValue)

		expect:
		velocity.getVelocity() != velocity.getMaxSpeed()

		when:
		velocity.cap()

		then:
		velocity.getVelocity() == velocity.getMaxSpeed()

		where:
		velocityValue << (2..20).collect {maxSpeedValue * it as float}
	}
	def "caps absolute value of all axes"() {
		velocity.getVelocity().set(velocityValue, velocityValue, velocityValue)

		expect:
		velocity.getVelocity() != velocity.getMaxSpeed()

		when:
		velocity.cap()

		then:
		Vector flip = new Vector(velocity.getVelocity())
		flip.scale(-1)
		flip == velocity.getMaxSpeed()

		where:
		velocityValue << (-1..-20).collect {maxSpeedValue * it as float}
	}
	def "leaves values <= cap alone"() {
		velocity.getVelocity().set(velocityComponent, velocityComponent, velocityComponent)
		Vector expectedVelocity = new Vector(velocity.getVelocity())

		when:
		velocity.cap()

		then:
		velocity.getVelocity() == expectedVelocity

		where:
		velocityComponent << (-maxSpeedValue..maxSpeedValue).collect {it as float}
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
