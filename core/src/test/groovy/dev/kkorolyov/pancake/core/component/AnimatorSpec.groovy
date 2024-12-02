package dev.kkorolyov.pancake.core.component

import dev.kkorolyov.pancake.core.animation.TransformFrame
import dev.kkorolyov.pancake.platform.animation.Choreography
import dev.kkorolyov.pancake.platform.animation.Timeline
import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Specification

class AnimatorSpec extends Specification {
	Choreography<TransformFrame> choreography = new Choreography<TransformFrame>().with {
		it.put("0", new Timeline<TransformFrame>().with {
			put(0, new TransformFrame())
			put(1, new TransformFrame().with {
				it.translation.set(Vector3.of(1, 1, 1))
				it
			})
			it
		})
		it.put("1", new Timeline<TransformFrame>().with {
			put(0, new TransformFrame())
			put(1, new TransformFrame().with {
				it.rotation.set(Vector3.of(1, 1, 1))
				it
			})
			it
		})
		it
	}

	Animator animator = new Animator()

	def "update returns null if no timelines"() {
		expect:
		animator.update(1) == null
	}
	def "update returns cumulative delta to next keyframe"() {
		when:
		animator.put(choreography.get("0"), Animator.Type.ONCE)
		animator.put(choreography.get("1"), Animator.Type.ONCE)

		then:
		animator.update(0) == new TransformFrame()
		animator.update(1000000) == new TransformFrame().with {
			it.translation.set(Vector3.of(1, 1, 1))
			it.rotation.set(Vector3.of(1, 1, 1))
			it
		}
	}

	def "clear removes timelines"() {
		when:
		animator.put(choreography.get("0"), Animator.Type.ONCE)
		animator.clear()

		then:
		animator.update(1) == null
	}

	// TODO Test remaining animation types
}
