package dev.kkorolyov.pancake.core.component.movement

import dev.kkorolyov.pancake.platform.math.Vector

import spock.lang.Shared
import spock.lang.Specification

class ForceSpec extends Specification {
	@Shared float mass = 1

	Force force = new Force(mass)

	def "returns accelerated vector"() {
		Vector velocity = new Vector()

		expect:
		force.accelerate(velocity, dt).is(velocity)

		where:
		dt << (0..100)
	}

	def "accelerates all velocity axes"() {
		force.getForce().set(forceComponent, forceComponent, forceComponent)
		Vector velocity = new Vector(1 ,1 ,1)
		Vector expectedVelocity = new Vector(velocity)
		expectedVelocity.add(force.getForce(), dt / mass as float)

		when:
		force.accelerate(velocity, dt)

		then:
		velocity == expectedVelocity

		where:
		forceComponent << (1..100)
		dt << (200..101)
	}
}
