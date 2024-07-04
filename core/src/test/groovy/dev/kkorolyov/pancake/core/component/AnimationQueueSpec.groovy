package dev.kkorolyov.pancake.core.component

import dev.kkorolyov.pancake.core.animation.TransformFrame
import dev.kkorolyov.pancake.platform.animation.Timeline
import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Specification

class AnimationQueueSpec extends Specification {
	AnimationQueue animationQueue = new AnimationQueue()

	def "update returns null if no timelines"() {
		expect:
		animationQueue.update(1) == null
	}
	def "update returns cumulative delta to next keyframe"() {
		when:
		animationQueue.add(new Timeline<TransformFrame>().with {
			put(0, new TransformFrame())
			put(1, new TransformFrame(Vector3.of(1, 1, 1), Vector3.of(), Vector3.of()))
			it
		}, AnimationQueue.Type.ONCE)
		animationQueue.add(new Timeline<TransformFrame>().with {
			put(0, new TransformFrame())
			put(1, new TransformFrame(Vector3.of(), Vector3.of(1, 1, 1), Vector3.of()))
			it
		}, AnimationQueue.Type.ONCE)

		then:
		animationQueue.update(0) == new TransformFrame()
		animationQueue.update(1000000) == new TransformFrame(Vector3.of(1, 1, 1), Vector3.of(1, 1, 1), Vector3.of())
	}

	def "reset returns null if no timelines"() {
		expect:
		animationQueue.reset() == null
	}
	def "reset returns null if no prior updates"() {
		when:
		animationQueue.add(new Timeline<TransformFrame>().with {
			put(0, new TransformFrame())
			put(1, new TransformFrame(Vector3.of(1, 1, 1), Vector3.of(), Vector3.of()))
			it
		}, AnimationQueue.Type.ONCE)

		then:
		animationQueue.reset() == null
	}
	def "reset returns cumulative delta to first keyframe"() {
		when:
		animationQueue.add(new Timeline<TransformFrame>().with {
			put(0, new TransformFrame())
			put(1, new TransformFrame(Vector3.of(1, 1, 1), Vector3.of(), Vector3.of()))
			it
		}, AnimationQueue.Type.ONCE)
		animationQueue.add(new Timeline<TransformFrame>().with {
			put(0, new TransformFrame())
			put(1, new TransformFrame(Vector3.of(), Vector3.of(1, 1, 1), Vector3.of()))
			it
		}, AnimationQueue.Type.ONCE)
		animationQueue.update(0)
		animationQueue.update(1000000)

		then:
		animationQueue.reset() == new TransformFrame(Vector3.of(-1, -1, -1), Vector3.of(-1, -1, -1), Vector3.of())
	}

	def "clear removes timelines"() {
		when:
		animationQueue.add(new Timeline<TransformFrame>().with {
			put(0, new TransformFrame())
			put(1, new TransformFrame(Vector3.of(1, 1, 1), Vector3.of(), Vector3.of()))
			it
		}, AnimationQueue.Type.ONCE)
		animationQueue.clear()

		then:
		animationQueue.update(1) == null
	}

	// TODO Test remaining animation types
}
