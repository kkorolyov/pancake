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

	def "update returns null when empty timeline"() {
		expect:
		new Playback<>(new Timeline<IntFrame>()).update(0) == null
	}

	def "1st update returns initial value"() {
		expect:
		playback.update(15) == new IntFrame(0)
	}
	def "2nd update returns diff to initial"() {
		when:
		playback.update(1)

		then:
		playback.update(2) == new IntFrame(2)
	}
	def "3rd update returns diff to 2nd"() {
		when:
		playback.update(1)
		playback.update(5)

		then:
		playback.update(3) == new IntFrame(3)
	}

	def "truncates to last timeline value when playback offset passes size"() {
		when:
		playback.update(0)

		then:
		playback.update(20) == new IntFrame(10)
	}
	def "returns null when done"() {
		when:
		playback.update(10)
		playback.update(10)

		then:
		playback.update(0) == null
		playback.update(1) == null
	}

	def "reset when initial returns no change"() {
		expect:
		playback.setOffset(0)
		playback.update(0) == new IntFrame(0)
	}
	def "reset returns diff to previous"() {
		when:
		playback.update(0)
		playback.update(4)

		then:
		playback.setOffset(0)
		playback.update(0) == new IntFrame(-4)
	}
	def "reset restarts playback"() {
		when:
		playback.update(0)
		playback.update(20)
		playback.setOffset(0)
		playback.update(0)

		then:
		playback.update(3) == new IntFrame(3)
	}
}
