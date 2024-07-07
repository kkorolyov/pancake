package dev.kkorolyov.pancake.core.io

import dev.kkorolyov.pancake.core.animation.TransformFrame
import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Specification

class AnimationStructizerSpec extends Specification {
	def structizer = new AnimationStructizer()

	def "toStructs TransformFrame"() {
		expect:
		structizer.toStruct(new TransformFrame(
				Vector3.of(x, y, z),
				Vector3.of(z, y, x),
				Vector3.of(x, z, y)
		)) == Optional.of([
				translation: [x.doubleValue(), y.doubleValue(), z.doubleValue()],
				rotation: [z.doubleValue(), y.doubleValue(), x.doubleValue()],
				scale: [x.doubleValue(), z.doubleValue(), y.doubleValue()]
		])

		where:
		x << (1..4)
		y << (4..1)
		z << (5..8)
	}
	def "fromStructs TransformFrame"() {
		expect:
		structizer.fromStruct(TransformFrame, [
				translation: [x, y, z],
				rotation: [z, y, x],
				scale: [x, z, y]
		]).get() == new TransformFrame(
				Vector3.of(x, y, z),
				Vector3.of(z, y, x),
				Vector3.of(x, z, y)
		)

		where:
		x << (1..4)
		y << (4..1)
		z << (5..8)
	}
	def "full-circles TransformFrame"() {
		expect:
		structizer.toStruct(new TransformFrame(
				Vector3.of(x, y, z),
				Vector3.of(z, y, x),
				Vector3.of(x, z, y)
		)).flatMap { structizer.fromStruct(TransformFrame, it) }
				.get() == new TransformFrame(
				Vector3.of(x, y, z),
				Vector3.of(z, y, x),
				Vector3.of(x, z, y)
		)

		where:
		x << (1..4)
		y << (4..1)
		z << (5..8)
	}
}
