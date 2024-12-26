package dev.kkorolyov.pancake.platform.animation

import spock.lang.Specification

class PlaybackSpec extends Specification {
	Timeline<IntFrame> timeline = new Timeline<IntFrame>().with {
		(0..10).forEach {
			put(it, new IntFrame(it))
		}
		it
	}

	Playback<IntFrame> playback = new Playback<>(timeline)

	def "setOffset returns null when empty timeline"() {
		expect:
		new Playback<>(new Timeline<IntFrame>()).setOffset(0) == null
	}
	def "1st setOffset returns diff to initial"() {
		expect:
		playback.setOffset(offset) == new IntFrame(expected)

		where:
		offset << [0, 1, 20]
		expected << [0, 1, 10]
	}
	def "2nd setOffset returns diff to previous"() {
		when:
		playback.setOffset(1)

		then:
		playback.setOffset(offset) == new IntFrame(expected)

		where:
		offset << [0, 1, 20]
		expected << [-1, 0, 9]
	}

	def "truncates to last timeline value when playback offset passes size"() {
		when:
		playback.setOffset(0)

		then:
		playback.setOffset(20) == new IntFrame(10)
	}
}
