package dev.kkorolyov.pancake.core.system

import dev.kkorolyov.pancake.core.animation.TransformFrame
import dev.kkorolyov.pancake.core.component.AnimationQueue
import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.platform.animation.Timeline
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.math.Matrix4
import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Specification

class AnimationSystemSpec extends Specification {
	EntityPool entities = new EntityPool()

	AnimationSystem system = new AnimationSystem()

	def "updates transform to match cumulative offset"() {
		def transform = new Transform()

		def animationQueue = new AnimationQueue()
		animationQueue.add(new Timeline<TransformFrame>().with {
			put(0, new TransformFrame(Vector3.of(1), Vector3.of(), Vector3.of(1)))
			it
		}, AnimationQueue.Type.ONCE)
		animationQueue.add(new Timeline<TransformFrame>().with {
			put(0, new TransformFrame(Vector3.of(0, 1), Vector3.of(0, 0, 1), Vector3.of()))
			it
		}, AnimationQueue.Type.ONCE)

		def entity = entities.create()
		entity.put(transform, animationQueue)

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
