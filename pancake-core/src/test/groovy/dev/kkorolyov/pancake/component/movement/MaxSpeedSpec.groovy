package dev.kkorolyov.pancake.component.movement

import dev.kkorolyov.pancake.math.Vector

import spock.lang.Shared
import spock.lang.Specification

class MaxSpeedSpec extends Specification {
	@Shared float maxSpeedValue = 15

	MaxSpeed maxSpeed = new MaxSpeed(maxSpeedValue)

	def "returns capped vector"() {
		Vector velocity = new Vector()

		expect:
		maxSpeed.cap(velocity).is(velocity)
	}

	def "caps all axes"() {
		Vector velocity = new Vector(velocityValue, velocityValue, velocityValue)

		expect:
		velocity != maxSpeed.getMaxSpeed()

		when:
		maxSpeed.cap(velocity)

		then:
		velocity == maxSpeed.getMaxSpeed()

		where:
		velocityValue << (2..20).collect {maxSpeedValue * it as float}
	}
	def "caps absolute value of all axes"() {
		Vector velocity = new Vector(velocityValue, velocityValue, velocityValue)

		expect:
		velocity != maxSpeed.getMaxSpeed()

		when:
		maxSpeed.cap(velocity)

		then:
		Vector flip = new Vector(velocity)
		flip.scale(-1)
		flip == maxSpeed.getMaxSpeed()

		where:
		velocityValue << (-1..-20).collect {maxSpeedValue * it as float}
	}
	def "leaves values <= cap alone"() {
		Vector velocity = new Vector(velocityComponent, velocityComponent, velocityComponent)
		Vector expectedVelocity = new Vector(velocity)

		when:
		maxSpeed.cap(velocity)

		then:
		velocity == expectedVelocity

		where:
		velocityComponent << (-maxSpeedValue..maxSpeedValue).collect {it as float}
	}
}
