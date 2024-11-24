package dev.kkorolyov.pancake.platform.animation

import spock.lang.Specification

class TimelineSpec extends Specification {
	Timeline<IntFrame> track = new Timeline().with {
		put(0, new IntFrame(0))
		put(100, new IntFrame(10))
		it
	}

	def "empty returns -1 size"() {
		expect:
		new Timeline<>().size() == -1
	}
	def "size returns greatest offset"() {
		expect:
		track.size() == 100

		when:
		track.put(10, new IntFrame(0))
		then:
		track.size() == 100

		when:
		track.put(240, new IntFrame(0))
		then:
		track.size() == 240
	}

	def "returns lerped value"() {
		expect:
		track.get(0) == new IntFrame(0)
		track.get(100) == new IntFrame(10)
		track.get(1) == new IntFrame(0)
		track.get(10) == new IntFrame(1)
		track.get(75) == new IntFrame(7)
		track.get(99) == new IntFrame(9)
	}

	def "throws on < 0 offset"() {
		when:
		track.get(-1)

		then:
		thrown(IllegalArgumentException)
	}
	def "throws on > size offset"() {
		when:
		track.get(101)

		then:
		thrown(IllegalArgumentException)
	}
}
