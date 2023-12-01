package dev.kkorolyov.pancake.graphics.io

import dev.kkorolyov.pancake.graphics.component.Lens
import dev.kkorolyov.pancake.platform.math.Vector2

import spock.lang.Ignore
import spock.lang.Specification

class ComponentStructizerSpec extends Specification {
	def structizer = new ComponentStructizer()

	def "toStructs Lens"() {
		expect:
		structizer.toStruct(new Lens(
				Vector2.of(*scale),
				Vector2.of(*size),
				Vector2.of(*offset),
				BitSet.valueOf(mask as long[]),
				active
		)) == Optional.of([
				scale: scale,
				size: size,
				offset: offset,
				mask: mask,
				active: active
		])

		where:
		scale << [[1d, 2d], [2d, 1d]]
		size << [[1d, 1d], [10d, 10d]]
		offset << [[0d, 0d], [14d, 15d]]
		mask << [[], [1l]]
		active << [false, true]
	}
	def "fromStructs partial Lens"() {
		when:
		def result = structizer.fromStruct(Lens, [
				scale: scale,
				size: size,
		]).get()

		then:
		result.scale == Vector2.of(*scale)
		result.size == Vector2.of(*size)

		where:
		scale << [[1d, 2d], [2d, 1d]]
		size << [[1d, 1d], [10d, 10d]]
	}
	def "fromStructs full Lens"() {
		when:
		def result = structizer.fromStruct(Lens, [
				scale: scale,
				size: size,
				offset: offset,
				mask: mask,
				active: active
		]).get()

		then:
		result.scale == Vector2.of(*scale)
		result.size == Vector2.of(*size)
		result.offset == Vector2.of(*offset)
		result.mask == BitSet.valueOf(mask as long[])
		result.active == active

		where:
		scale << [[1d, 2d], [2d, 1d]]
		size << [[1d, 1d], [10d, 10d]]
		offset << [[0d, 0d], [14d, 15d]]
		mask << [[], [1l]]
		active << [false, true]
	}
	def "full-circles Lens"() {
		when:
		def result = structizer.toStruct(new Lens(
				Vector2.of(*scale),
				Vector2.of(*size),
				Vector2.of(*offset),
				BitSet.valueOf(mask as long[]),
				active
		)).flatMap { structizer.fromStruct(Lens, it) }
				.get()

		then:
		result.scale == Vector2.of(*scale)
		result.size == Vector2.of(*size)
		result.offset == Vector2.of(*offset)
		result.mask == BitSet.valueOf(mask as long[])
		result.active == active

		where:
		scale << [[1d, 2d], [2d, 1d]]
		size << [[1d, 1d], [10d, 10d]]
		offset << [[0d, 0d], [14d, 15d]]
		mask << [[], [1l]]
		active << [false, true]
	}

	@Ignore
	def "toStructs Model"() {
		// TODO
	}
	@Ignore
	def "fromStructs Model"() {
		// TODO
	}
	@Ignore
	def "full-circles Model"() {
		// TODO
	}
}
