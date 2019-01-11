package dev.kkorolyov.pancake.core.action

import dev.kkorolyov.pancake.core.component.movement.Force
import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.Signature
import dev.kkorolyov.pancake.platform.math.Vector

import spock.lang.Shared
import spock.lang.Specification

import static dev.kkorolyov.pancake.platform.SpecUtilities.randVector

class ForceActionSpec extends Specification {
	@Shared Vector force = randVector()
	@Shared Signature signature = new Signature(Force)

	Vector forceForce = Mock()
	Force forceComponent = Mock(Force) {
		getForce() >> forceForce
	}
	Entity entity = Mock() {
		get(Force) >> forceComponent
	}

	Action action = new ForceAction(force)

	def "adds force"() {
		when:
		action.apply(entity)

		then:
		1 * forceForce.add(force)
	}
}
