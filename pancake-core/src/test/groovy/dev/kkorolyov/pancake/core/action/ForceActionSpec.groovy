package dev.kkorolyov.pancake.core.action

import dev.kkorolyov.pancake.core.component.movement.Force
import dev.kkorolyov.pancake.platform.SpecUtilities
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.Signature
import dev.kkorolyov.pancake.platform.math.Vector

import spock.lang.Shared
import spock.lang.Specification

class ForceActionSpec extends Specification {
	@Shared Vector force = SpecUtilities.randVector()
	@Shared Signature signature = new Signature(Force)

	Vector forceForce = Mock()
	Force forceComponent = Mock() {
		getForce() >> forceForce
	}
	Entity entity = Mock() {
		contains(signature) >> true
		get(Force) >> forceComponent
	}

	ForceAction action = new ForceAction(force)

	def "adds force"() {
		when:
		action.accept(entity)

		then:
		1 * forceForce.add(force)
	}
}
