package dev.kkorolyov.pancake.core.system

import dev.kkorolyov.pancake.core.animation.TransformFrame
import dev.kkorolyov.pancake.core.component.Animator
import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.platform.animation.Choreography
import dev.kkorolyov.pancake.platform.animation.Timeline
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.math.Matrix4
import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Specification

class AnimationSystemSpec extends Specification {
	EntityPool entities = new EntityPool()
	Choreography<TransformFrame> choreography = new Choreography<TransformFrame>().with {
		it.put("0", new Timeline<TransformFrame>().with {
			put(0, new TransformFrame().with {
				it.translation.set(Vector3.of(1))
				it.scale.set(Vector3.of(1))
				it
			})
			it
		})
		it.put("1", new Timeline<TransformFrame>().with {
			put(0, new TransformFrame().with {
				it.translation.set(Vector3.of(0, 1))
				it.rotation.set(Vector3.of(0, 0, 1))
				it
			})
			it
		})
		it
	}
	AnimationSystem system = new AnimationSystem()

	def "updates transform to match cumulative offset"() {
		def transform = new Transform()

		def animator = new Animator()
		animator.put(choreography.get("0"), Animator.Type.ONCE)
		animator.put(choreography.get("1"), Animator.Type.ONCE)

		def entity = entities.create()
		entity.put(transform, animator)

		when:
		system.update(entity, 0)
		system.after(0)

		then:
		transform.getTranslation() == Vector3.of(1, 1)
		transform.getRotation() == Matrix4.of().with {
			rotate(1, Vector3.of(0, 0, 1))
			it
		}
		transform.scale == Vector3.of(2, 1, 1)
	}
}
