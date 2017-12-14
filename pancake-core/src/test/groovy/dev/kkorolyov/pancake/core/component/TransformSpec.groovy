package dev.kkorolyov.pancake.core.component

import dev.kkorolyov.pancake.platform.math.Vector

import spock.lang.Shared
import spock.lang.Specification

import static dev.kkorolyov.pancake.platform.SpecUtilities.randVector

class TransformSpec extends Specification {
	@Shared Vector position = randVector()
	@Shared Vector orientation = randVector()
	@Shared Vector parentGlobalPosition = randVector()
	@Shared Vector parentGlobalOrientation = randVector()
	Transform parent = Mock() {
		getGlobalPosition() >> parentGlobalPosition
		getGlobalOrientation() >> parentGlobalOrientation
	}

	Transform transform = new Transform(position, orientation)

	def "global position adds local to parent global position"() {
		when:
		transform.setParent(parent, false)

		then:
		transform.getGlobalPosition() == Vector.add(position, parentGlobalPosition)
	}
	def "global position adds local to parent global position with pivot"() {
		when:
		transform.setParent(parent, true)

		then:
		transform.getGlobalPosition() == new Vector(position)
				.pivot(parentGlobalOrientation.getZ(), parentGlobalOrientation.getX())
				.add(parentGlobalPosition)
	}

	def "global orientation is local orientation when not rotatesWithParent"() {
		when:
		transform.setParent(parent, false)

		then:
		transform.getGlobalOrientation() == orientation
	}
	def "global orientation adds local to parent global orientation"() {
		when:
		transform.setParent(parent, true)

		then:
		transform.getGlobalOrientation() == Vector.add(orientation, parentGlobalOrientation)
	}
}
