package dev.kkorolyov.pancake.core.component.movement

import dev.kkorolyov.pancake.platform.math.Vector3
import dev.kkorolyov.pancake.platform.math.Vectors

import spock.lang.Shared
import spock.lang.Specification

class DampingSpec extends Specification {
	@Shared
	Vector3 value = Vectors.create(0.5, 0.5, 0.5)
	@Shared
	double mini = 0.001
	@Shared
	double micro = 0.000001

	Damping damping = new Damping(value)

	def "damps when force zero"() {
		Vector3 velocity = Vectors.create(1, 1, 1)
		Vector3 force = Vectors.create(0, 0, 0)

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
		velocity << (1..3).collect { it -> Vectors.create(mini, mini, mini) }
		force << [Vectors.create(-micro, micro, micro), Vectors.create(micro, -micro, micro), Vectors.create(micro, micro, -micro)]
	}
	def "does not damp where force non-zero and same sign as velocity"() {
		when:
		damping.damp(velocity, force)

		then:
		velocity.x == ((force.getX() > 0) ? mini : value.x * mini)
		velocity.y == ((force.getY() > 0) ? mini : value.y * mini)
		velocity.z == ((force.getZ() > 0) ? mini : value.z * mini)

		where:
		velocity << (1..3).collect { it -> Vectors.create(mini, mini, mini) }
		force << [Vectors.create(micro, 0, 0), Vectors.create(0, micro, 0), Vectors.create(0, 0, micro)]
	}
}
