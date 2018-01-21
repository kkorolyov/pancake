package dev.kkorolyov.pancake.core.action

import dev.kkorolyov.pancake.core.component.movement.Force
import dev.kkorolyov.pancake.platform.SpecUtilities
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.entity.Signature
import dev.kkorolyov.pancake.platform.math.Vector

import spock.lang.Shared
import spock.lang.Specification

import static dev.kkorolyov.pancake.platform.SpecUtilities.randInt

class ForceActionSpec extends Specification {
	@Shared Vector force = SpecUtilities.randVector()
	@Shared Signature signature = new Signature(Force)

	Vector forceForce = Mock()
	Force forceComponent = Mock() {
		getForce() >> forceForce
	}
	int id = randInt()
	EntityPool entities = Mock() {
		contains(id, signature) >> true
		get(id, Force) >> forceComponent
	}

	ForceAction action = new ForceAction(force)

	def "adds force"() {
		when:
		action.accept(id, entities)

		then:
		1 * forceForce.add(force)
	}
}
