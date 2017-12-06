package dev.kkorolyov.pancake.core.component

import dev.kkorolyov.pancake.platform.media.CompositeImage

import spock.lang.Shared
import spock.lang.Specification

class SpriteSpec extends Specification {	// TODO Too many inline values
	@Shared CompositeImage image = Mock() {
		it.getSize() >> new Vector(1, 1)
	}
	@Shared float frameInterval = 1

	Sprite sprite = new Sprite(image)

	def "resets to frame 0 on stop with reset=true"() {
		sprite = new Sprite(image, 2, 2, 0)
		sprite.setFrame(3)

		expect:
		sprite.getFrame() == 3

		when:
		sprite.stop(false, true)
		then:
		sprite.getFrame() == 0
	}

	def "steps 1 frame each frameInterval"() {
		sprite = new Sprite(image, 3, 3, frameInterval)

		when:
		sprite.tick(frameInterval)
		then:
		sprite.getFrame() == 1

		when:
		sprite.tick(frameInterval * 2 as float)
		then:
		sprite.getFrame() == 3
	}
	def "negative frameInterval steps backwards"() {
		sprite = new Sprite(image, 2, 2, -frameInterval)

		when:
		sprite.tick(frameInterval)
		then:
		sprite.getFrame() == 3
		
		when:
		sprite.tick(frameInterval * 2 as float)
		then:
		sprite.getFrame() == 1
	}
	def "0 frameInterval is stopped"() {
		sprite = new Sprite(image, 2, 2, 0)

		expect:
		sprite.isStopped()
	}

	def "does not update if stopped"() {
		sprite = new Sprite(image, 2, 2, frameInterval)

		when:
		sprite.stop(true, false)
		sprite.tick(frameInterval)

		then:
		sprite.getFrame() == 0
	}
}
