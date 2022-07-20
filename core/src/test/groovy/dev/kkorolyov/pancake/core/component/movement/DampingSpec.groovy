package dev.kkorolyov.pancake.core.component.movement

import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Shared
import spock.lang.Specification

class DampingSpec extends Specification {
	@Shared
	Vector3 value = Vector3.of(0.5, 0.5, 0.5)
	@Shared
	double mini = 0.001
	@Shared
	double micro = 0.000001

	Damping damping = new Damping(value)

	def "damps when force zero"() {
		Vector3 velocity = Vector3.of(1, 1, 1)
		Vector3 force = Vector3.of(0, 0, 0)

		when:
		damping.damp(velocity, force)

		then:
		velocity == value
	}
	def "damps where force opposite sign of velocity"() {
		when:
		damping.damp(velocity, force)

		then:
		velocity.x == ((force.getX() < 0) ? value.x * mini : mini)
		velocity.y == ((force.getY() < 0) ? value.y * mini : mini)
		velocity.z == ((force.getZ() < 0) ? value.z * mini : mini)

		where:
		velocity << (1..3).collect { it -> Vector3.of(mini, mini, mini) }
		force << [Vector3.of(-micro, micro, micro), Vector3.of(micro, -micro, micro), Vector3.of(micro, micro, -micro)]
	}
	def "does not damp where force non-zero and same sign as velocity"() {
		when:
		damping.damp(velocity, force)

		then:
		velocity.x == ((force.getX() > 0) ? mini : value.x * mini)
		velocity.y == ((force.getY() > 0) ? mini : value.y * mini)
		velocity.z == ((force.getZ() > 0) ? mini : value.z * mini)

		where:
		velocity << (1..3).collect { it -> Vector3.of(mini, mini, mini) }
		force << [Vector3.of(micro, 0, 0), Vector3.of(0, micro, 0), Vector3.of(0, 0, micro)]
	}
}
