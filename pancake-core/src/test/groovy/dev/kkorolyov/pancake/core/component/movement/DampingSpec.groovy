package dev.kkorolyov.pancake.core.component.movement

import dev.kkorolyov.pancake.platform.math.Vector

import spock.lang.Shared
import spock.lang.Specification

class DampingSpec extends Specification {
	@Shared float dampValue = 0.5
	@Shared float mini = 0.001
	@Shared float micro = 0.000001

	Damping damping = new Damping(dampValue)

	def "returns damped vector"() {
		Vector velocity = new Vector()

		expect:
		damping.damp(velocity, new Vector()).is(velocity)
	}

	def "damps all velocity axes when force zero"() {
		Vector velocity = new Vector(1, 1, 1)
		Vector force = new Vector()
		Vector expectedVelocity = new Vector(dampValue,
				dampValue,
				dampValue)

		when:
		damping.damp(velocity, force)

		then:
		velocity == expectedVelocity
	}
	def "damps velocity axes where force opposite sign of velocity"() {
		when:
		damping.damp(velocity, force)

		then:
		velocity.getX() == ((force.getX() < 0) ? dampValue * mini : mini)
		velocity.getY() == ((force.getY() < 0) ? dampValue * mini : mini)
		velocity.getZ() == ((force.getZ() < 0) ? dampValue * mini : mini)

		where:
		velocity << (1..3).collect {it -> new Vector(mini, mini, mini)}
		force << [new Vector(-micro, micro, micro), new Vector(micro, -micro, micro), new Vector(micro, micro, -micro)]
	}
	def "does not damp velocity axes where force non-zero and same sign as velocity"() {
		when:
		damping.damp(velocity, force)

		then:
		velocity.getX() == ((force.getX() > 0) ? mini : dampValue * mini)
		velocity.getY() == ((force.getY() > 0) ? mini : dampValue * mini)
		velocity.getZ() == ((force.getZ() > 0) ? mini : dampValue * mini)

		where:
		velocity << (1..3).collect {it -> new Vector(mini, mini, mini)}
		force << [new Vector(micro, 0, 0), new Vector(0, micro, 0), new Vector(0, 0, micro)]
	}

	def "constrains damping to >= 0"() {
		when:
		damping.setDamping(value)
		then:
		damping.getDamping() == new Vector()

		when:
		damping.setDamping(value, value)
		then:
		damping.getDamping() == new Vector()

		when:
		damping.setDamping(value, value, value)
		then:
		damping.getDamping() == new Vector()

		where:
		value << (-1000..0)
	}
	def "constrains damping to <= 1"() {
		when:
		damping.setDamping(value)
		then:
		damping.getDamping() == new Vector(1, 1, 1)

		when:
		damping.setDamping(value, value)
		then:
		damping.getDamping() == new Vector(1, 1, 1)

		when:
		damping.setDamping(value, value, value)
		then:
		damping.getDamping() == new Vector(1, 1, 1)

		where:
		value << (1..1000)
	}
}
